package com.caimao.weixin.note.controller;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.domain.Focus;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Stat;
import com.caimao.weixin.note.domain.Ticker;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.domain.WxShareInfo;
import com.caimao.weixin.note.service.BankService;
import com.caimao.weixin.note.service.CapitalService;
import com.caimao.weixin.note.service.CommentService;
import com.caimao.weixin.note.service.DepositService;
import com.caimao.weixin.note.service.FocusService;
import com.caimao.weixin.note.service.NoteService;
import com.caimao.weixin.note.service.ReaderService;
import com.caimao.weixin.note.service.SmsService;
import com.caimao.weixin.note.service.StatService;
import com.caimao.weixin.note.service.UserService;
import com.caimao.weixin.note.service.WithdrawService;
import com.caimao.weixin.note.util.DateUtil;
import com.caimao.weixin.note.util.Pager;
import com.caimao.weixin.note.util.RedisUtil;
import com.caimao.weixin.note.util.StockUtil;
import com.caimao.weixin.note.util.StringUtil;
import com.caimao.weixin.note.util.WXUtil;

/**
 * BaseController
 */
public class BaseController {

	/** LOGGER */
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	@Value("${holiday}")
	protected String holiday;

	@Value("${picturePath}")
	protected String savePicPath;
	
	@Value("${qrCodePicturePath}")
	protected String saveQrCodePicPath;

	@Value("${wx.test}")
	private String wxTest;

	@Value("${wx.domain}")
	protected String domain;

	/** 更新数据库中微信用户的基本信息时间（单位：小时） */
	public static Long updateDbWeixinUserInfoHours = 24L;

	/** web授权作用域 */
	public static String scopeBase = "snsapi_base";
	public static String scopeUser = "snsapi_userinfo";

	protected Pager pager;
	protected Capital capital;
	protected Note note;
	protected Focus focus;

	@Autowired
	protected RedisUtil redisUtil;

	@Autowired
	protected WXUtil wxUtil;

	@Autowired
	protected NoteService noteService;

	@Autowired
	protected StatService statService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected DepositService depositService;

	@Autowired
	protected ReaderService readerService;

	@Autowired
	protected BankService bankService;

	@Autowired
	protected WithdrawService withdrawService;

	@Autowired
	protected SmsService smsService;

	@Autowired
	protected CapitalService capitalService;
	
	@Autowired
	protected CommentService commentService;

	@Autowired
	protected FocusService focusService;

	/**
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	protected User baseAuthInfo(ModelAndView mav) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		// 根据网页传递 范围，定义范围
		String scope = request.getParameter("state");
		if (scope == null)
			scope = scopeUser;
		if (!(scope.equals(scopeBase) || scope.equals(scopeUser))) {
			scope = scopeBase;
		}

		String code = request.getParameter("code");
		if (null != code) {
			this.wxUtil.saveAccessTokenAndOpenIdByCode(code, scope);
		}
		Map<String, String> authInfo = this.wxUtil.verificationAccessToken(scope);
		// 如果返回null，则跳转到授权页面
		if (null == authInfo) {
			return null;
		}
		mav.addObject("authInfo", authInfo);

		// 获取JS SDK 需要的配置
		mav.addObject("jsApiTicket", this.wxUtil.getJsApiTicket(this.wxUtil.getNowUrl(false)));

		// 默认先设置为关注
		mav.addObject("subscribe", 1);
		// 根据Web授权，获取用户信息，如果没有注册，则注册到数据库
		Map<String, String> webUserInfo = this.wxUtil.getWebUserInfoByOpenId(authInfo.get("accessToken"), authInfo.get("openId"), scope);
		if (webUserInfo == null)
			return null;
		try {
			Map<String, String> wxUserInfo = this.wxUtil.getUserInfoByOpenId(authInfo.get("openId"));
			if (wxUserInfo != null) {
				mav.addObject("subscribe", wxUserInfo.get("subscribe"));
				webUserInfo.put("subscribe", wxUserInfo.get("subscribe"));
			}
		} catch (Exception ignored) {
		}

		User user = registerNoteNewUser(webUserInfo);
		mav.addObject("user", user);

		if (null != user) {
			/** 所有页面默认分享信息，如需要特殊定制，请在单个的方法中设置并覆盖 */
			WxShareInfo shareInfo = new WxShareInfo();
			shareInfo.setTitle(user.getUser_nickname() + "的投资笔记");
			shareInfo.setDesc(user.getUser_nickname() + "一共写了" + user.getUser_note_count() + "篇投资笔记,平均收益率" + user.getUser_yield() + "%");
			shareInfo.setFriend("");
			shareInfo.setImgUrl(user.getUser_header_img());
			shareInfo.setLink(this.domain + "/weixin/note/author?userId=" + user.getUser_id());
			mav.addObject("shareInfo", shareInfo);
		}
		return user;
	}

	// 获取统计数据
	protected void getStat(ModelAndView mav) {
		if (null == redisUtil.get("wx_note_count") || null == redisUtil.get("wx_note_readerCount")) {
			Stat stat = statService.findById(1);
			redisUtil.set("wx_note_count", String.valueOf(stat.getStat_note()), 6 * 60 * 60L); // 6个小时
			redisUtil.set("wx_note_readerCount", String.valueOf(stat.getStat_reader()), 6 * 60 * 60L); // 6个小时
		}
		mav.addObject("wx_note_count", redisUtil.get("wx_note_count"));
		mav.addObject("wx_note_readerCount", redisUtil.get("wx_note_readerCount"));
	}

	/** 获取股票名称 */
	@RequestMapping(value = "getStock", method = RequestMethod.GET)
	public String getStock(@RequestParam(value = "code", required = true) String code) throws Exception {
		if ("".equals(code) || null == code) {
			return "股票代码不能为空！";
		}
		Pattern pattern = Pattern.compile("^[0-9]{6}$");
		Matcher matcher = pattern.matcher(code);
		if (!matcher.matches()) {
			return "不是有效的股票代码！";
		}
		Ticker ticker = StockUtil.getTicket(code);
		if (null == ticker) {
			return "股票不存在！";
		} else {
			if (ticker.getName().toUpperCase().startsWith("N")) {
				return "不可发新股！";
			}
			return ticker.getName() + "，" + ticker.getCurrentPrice();
		}
	}

	/** 根据天数返回开始时间和结束时间 */
	@RequestMapping(value = "getBetweenDate", method = RequestMethod.GET)
	public String getBetweenDate(@RequestParam(value = "d", required = true) String day) throws Exception {
		int d = Integer.valueOf(day);
		if (0 == d) {
			return "";
		}
		String start = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", DateUtil.getAfterWorkDay("00:00:00", 1, holiday)).substring(0, 10);
		String end = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", DateUtil.getAfterWorkDay("00:00:00", d, holiday)).substring(0, 10);
		return start + "," + end;
	}

	/**
	 * 根据网页获取的webUserInfo 信息，查找数据库中用户，没有则注册
	 * 
	 * @param wxUser
	 */
	protected User registerNoteNewUser(Map<String, String> wxUser) throws Exception {
		String openId = wxUser.get("openid");
		if (null == openId) {
			this.wxUtil.verificationAccessToken(scopeUser);
			return null;
		}
		User user = userService.findByOpenId(openId);
		if (null == user) {
			if (wxUser.get("nickname") == null) {
				this.wxUtil.verificationAccessToken(scopeUser);
				return null;
			}
			User u = new User();
			u.setUser_open_id(wxUser.get("openid"));
			u.setUser_nickname(StringUtil.filterWeiXinEmojiChar(wxUser.get("nickname")));
			u.setUser_header_img(wxUser.get("headimgurl"));
			u.setUser_subscribe(wxUser.get("subscribe") == null ? 0 : Integer.valueOf(wxUser.get("subscribe")));
			u.setUser_note_count(0);
			u.setUser_success(0);
			u.setUser_yield(0);
			u.setUser_phone("");
			u.setUser_bank_code("");
			u.setUser_available_money(0);
			u.setUser_freeze_money(0);
			u.setUser_update_time(new Date());
			u.setUser_create_time(new Date());
			userService.save(u);
			user = userService.findByOpenId(openId);
		} else {
			updateUserInfo(user);
		}

		return user;
	}

	/**
	 * 更新数据库中用户信息（昵称、头像），如果最后更新时间大于24小时的，更新一下
	 */
	protected void updateUserInfo(User user) {
		try {
			if (new Date().getTime() - user.getUser_update_time().getTime() >= (updateDbWeixinUserInfoHours * 60 * 60 * 1000)) {
				Map<String, String> wxMap = this.wxUtil.getUserInfoByOpenId(user.getUser_open_id());
				if (null == wxMap) {
					return;
				}
				if (null == wxMap.get("nickname")) return;
				User u = new User();
				u.setUser_id(user.getUser_id());
				u.setUser_open_id(user.getUser_open_id());
				u.setUser_nickname(StringUtil.filterWeiXinEmojiChar(wxMap.get("nickname")));
				u.setUser_header_img(wxMap.get("headimgurl"));
				u.setUser_subscribe(null == wxMap.get("subscribe") ? 0 : Integer.valueOf(wxMap.get("subscribe")));
				u.setUser_note_count(user.getUser_note_count());
				u.setUser_success(user.getUser_success());
				u.setUser_yield(user.getUser_yield());
				u.setUser_phone(user.getUser_phone());
				u.setUser_bank_code(user.getUser_bank_code());
				u.setUser_available_money(user.getUser_available_money());
				u.setUser_freeze_money(user.getUser_freeze_money());
				u.setUser_create_time(user.getUser_create_time());
				u.setUser_update_time(new Date());
				userService.update(u);
			}
		} catch (Exception e) {
			LOGGER.error(" 更新数据库中微信用户信息异常 {}", e);
		}
	}

	/** 分享信息 */
	protected void setShareInfo(ModelAndView mav, User user) {
		WxShareInfo shareInfo = new WxShareInfo();
		shareInfo.setTitle(user.getUser_nickname() + "的投资笔记");
		shareInfo.setDesc(user.getUser_nickname() + "一共写了" + user.getUser_note_count() + "篇投资笔记,平均收益率" + user.getUser_yield() + "%");
		shareInfo.setFriend(user.getUser_nickname() + "一共写了" + user.getUser_note_count() + "篇投资笔记,平均收益率" + user.getUser_yield() + "%");
		shareInfo.setImgUrl(user.getUser_header_img());
		shareInfo.setLink(this.domain + "/weixin/note/author?userId=" + user.getUser_id());
		mav.addObject("shareInfo", shareInfo);
	}

}

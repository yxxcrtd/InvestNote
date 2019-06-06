package com.caimao.weixin.note.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.caimao.weixin.note.domain.Focus;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.domain.Ticker;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.enums.EDepositType;
import com.caimao.weixin.note.enums.ENotePayStatus;
import com.caimao.weixin.note.util.DateUtil;
import com.caimao.weixin.note.util.Pager;
import com.caimao.weixin.note.util.QRcodeUtil;
import com.caimao.weixin.note.util.StockUtil;
import com.caimao.weixin.note.util.StringHelper;

@RestController
@RequestMapping("/weixin/note")
public class NoteController extends BaseController {

	/**
	 * Index
	 */
	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	public ModelAndView index(@RequestParam(value = "p", required = true, defaultValue = "1") int p) throws Exception {
		ModelAndView mav = new ModelAndView("index/MyFocus");
		User user = baseAuthInfo(mav);
		if (null == user) {
			return null;
		}
		pager = new Pager();
		pager.setPageNo(p);
		int count = noteService.findMyFocusNotesCount(user.getUser_id());
		pager.setTotalCount(count);
		mav.addObject("count", pager.getTotalPageCount());
		mav.addObject("p", p);
		mav.addObject("list", noteService.findMyFocusNotesWithPage(user.getUser_id(), pager));
		return mav;
	}

	/**
	 * Index Ajax
	 */
	@RequestMapping(value = "ajax", method = RequestMethod.GET)
	public String indexAjax(@RequestParam(value = "p", required = true, defaultValue = "1") int p) throws Exception {
		ModelAndView mav = new ModelAndView();
		User user = baseAuthInfo(mav);
		if (null == user) {
			return null;
		}
		pager = new Pager();
		pager.setPageNo(p);
		int count = noteService.findMyFocusNotesCount(user.getUser_id());
		pager.setTotalCount(count);
		JSONObject obj = new JSONObject();
		obj.put("list", noteService.findMyFocusNotesWithPage(user.getUser_id(), pager));
		obj.put("p", p);
		return obj.toString();
	}

	/**
	 * Edit
	 * 
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.GET)
	public ModelAndView edit() throws Exception {
		ModelAndView mav = new ModelAndView("note/NoteEdit");
		// 每个页面都需要加的授权信息
		baseAuthInfo(mav);
		Note note = new Note();
		mav.addObject("obj", note);
		return mav;
	}

	/**
	 * Save
	 * 
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("obj") @Valid Note note, BindingResult result) throws Exception {
		ModelAndView mav = new ModelAndView("note/NoteEdit");
		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (null == user) {
			return null;
		}

		if (result.hasErrors()) {
			note.setNote_open_money(note.getNote_open_money());
			mav.addObject("obj", note);
			return mav;
		}

		Date date = new Date();
		note.setNote_user_id(user.getUser_id());
		note.setNote_packet_money(note.getNote_open_money()); // 等额的红包
		note.setNote_start_time(DateUtil.getAfterWorkDay(DateUtil.getNoteStartTime(date), 1, holiday));
		note.setNote_end_time(DateUtil.getAfterWorkDay("15:00:00", note.getNote_target_day(), holiday));
		note.setNote_pay_status(ENotePayStatus.INIT.getCode());
		note.setNote_create_time(date);

		// 从接口中获取股票的信息
		Ticker ticker = StockUtil.getTicket(note.getNote_stock_code());
		if (null != ticker) {
			if (ticker.getName().toUpperCase().startsWith("N")) {
				mav.addObject("obj", note);
				mav.addObject("stockName", "不可发新股！");
				return mav;
			}
			note.setNote_stock_name(ticker.getName());
		} else {
			mav.addObject("obj", note);
			mav.addObject("stockName", "股票不存在！");
			return mav;
		}

		// 保存，并返回刚插入的主键ID
		Long newId = noteService.saveReturnId(note);
		LOGGER.info("笔记ID：{}（{}，{}）的创建时间：{}", newId, note.getNote_stock_name(), note.getNote_stock_code(), date);

		// 如果阅读费为0的话，直接使用余额支付，并跳转到分享页面
		if (0 == note.getNote_open_money()) {
			return new ModelAndView("redirect:/weixin/note/pay/yuePay?noteId=" + newId + "&optType=" + EDepositType.NOTE.getCode());
		} else {
			// 跳转到支付页面
			return new ModelAndView(String.format("redirect:/weixin/note/pay/pay?noteId=%s&optType=%s", newId, EDepositType.NOTE.getCode()));
		}
	}

	/**
	 * 我的主页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/my", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView profile() throws Exception {
		ModelAndView mav = new ModelAndView("/note/NoteIndex");
		User user = this.baseAuthInfo(mav);
		if (null == user) {
			return null;
		}
		mav.addObject("focus", focusService.findFocusCountByUserId("focus_user_id", user.getUser_id()));
		mav.addObject("fans", focusService.findFocusCountByUserId("focus_other_id", user.getUser_id()));
		return mav;
	}

	/**
	 * 我创建的所有笔记
	 *
	 * @param p
	 *            分页参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/my_note", method = RequestMethod.GET)
	public ModelAndView my(@RequestParam(value = "p", required = true, defaultValue = "1") int p) throws Exception {
		ModelAndView mav = new ModelAndView("/note/NoteMy");

		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (null == user) {
			return null;
		}

		String where = "note_user_id = " + user.getUser_id();
		pager = new Pager();
		pager.setPageNo(p);
		int count = noteService.findAllCount(new Note(), where);
		pager.setTotalCount(count);
		mav.addObject("count", pager.getTotalPageCount());
		mav.addObject("list", noteService.findByPager(pager, where, "note_status, note_id DESC, note_start_time DESC"));
		mav.addObject("p", p);
		return mav;
	}

	/**
	 * 我创建的所有笔记的分页
	 */
	@RequestMapping(value = "my_note/ajax", method = RequestMethod.GET)
	public String ajax(@RequestParam(value = "p", required = true, defaultValue = "1") int p) throws Exception {
		ModelAndView mav = new ModelAndView();

		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (null == user) {
			return null;
		}

		String where = "note_user_id = " + user.getUser_id();
		pager = new Pager();
		pager.setPageNo(p);
		int count = noteService.findAllCount(new Note(), where);
		pager.setTotalCount(count);
		JSONObject obj = new JSONObject();
		obj.put("list", noteService.findByPager(pager, where, "note_status, note_id DESC, note_start_time DESC"));
		obj.put("p", p);
		return obj.toString();
	}

	/**
	 * 作者页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/author", method = RequestMethod.GET)
	public ModelAndView author(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "p", required = false, defaultValue = "1") Integer p, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("/note/author");
		User user = this.baseAuthInfo(mav);
		if (user == null)
			return null;

		User authorUser = this.userService.findById(userId);
		if (authorUser == null) {
			return new ModelAndView("redirect:/" + request.getContextPath() + "/my_node");
		}
		// 分页获取作者的笔记列表
		String where = "note_user_id = " + authorUser.getUser_id() + " AND note_pay_status = 2";
		Pager pager = new Pager();
		pager.setPageNo(p);
		Note note = new Note();
		int count = noteService.findAllCount(note, where);
		pager.setTotalCount(count);
		mav.addObject("count", pager.getTotalPageCount());

		List<Note> noteList = noteService.findByPager(pager, where, "note_status, note_id DESC, note_create_time DESC");
		for (Note n : noteList) {
			if (n.getNote_status() != 2) {
				if (n.getNote_user_id() != user.getUser_id()) {
					if (n.getNote_open_money() > 0) {
						List<Reader> readerList = this.readerService.findReaderByNoteIdAndUserId(n.getNote_id(), user.getUser_id());
						if (readerList == null || readerList.size() == 0) {
							n.setNote_stock_name("******");
							n.setNote_stock_code(StringHelper.hide(n.getNote_stock_code(), 3, 0));
						}
					}
				}
			}
		}
		mav.addObject("list", noteList);
		mav.addObject("authorUser", authorUser);
		mav.addObject("p", p);
		mav.addObject("userId", userId);
		mav.addObject("uId", user.getUser_id());
		// 分享信息
		setShareInfo(mav, authorUser);
		mav.addObject("focus", focusService.findFocusCountByUserId("focus_user_id", userId));
		mav.addObject("fans", focusService.findFocusCountByUserId("focus_other_id", userId));
		focus = focusService.findFocusByUserIdAndFocusId(user.getUser_id(), Integer.valueOf(userId));
		if (null == focus) {
			mav.addObject("isFocus", false);
		} else {
			mav.addObject("isFocus", true);
		}
		return mav;
	}

	/**
	 * 作者页面的分页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/author/ajax", method = RequestMethod.GET)
	public String authorAjax(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "p", required = false, defaultValue = "1") Integer p, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("/note/author");
		User user = this.baseAuthInfo(mav);
		if (user == null)
			return null;

		User authorUser = this.userService.findById(userId);
		if (authorUser == null) {
			return "";
		}
		// 分页获取作者的笔记列表
		String where = "note_user_id = " + authorUser.getUser_id() + " AND note_pay_status = 2";
		Pager pager = new Pager();
		pager.setPageNo(p);
		Note note = new Note();
		int count = noteService.findAllCount(note, where);
		pager.setTotalCount(count);

		List<Note> noteList = noteService.findByPager(pager, where, "note_status, note_id DESC, note_create_time DESC");
		for (Note n : noteList) {
			if (n.getNote_status() != 2) {
				if (n.getNote_user_id() != user.getUser_id()) {
					if (n.getNote_open_money() > 0) {
						List<Reader> readerList = this.readerService.findReaderByNoteIdAndUserId(n.getNote_id(), user.getUser_id());
						if (readerList == null || readerList.size() == 0) {
							n.setNote_stock_name("******");
							n.setNote_stock_code(StringHelper.hide(n.getNote_stock_code(), 3, 0));
						}
					}
				}
			}
		}

		JSONObject obj = new JSONObject();
		obj.put("list", noteList);
		obj.put("p", p);
		obj.put("userId", userId);
		obj.put("authorUser", authorUser);
		return obj.toString();
	}
	
	//根据noteid生成二维码
	@RequestMapping(value = "qrcode//{id:[\\d]+}", method = RequestMethod.GET)
	public ModelAndView qrCode(@PathVariable(value = "id") int noteId) throws Exception {
		ModelAndView mav = new ModelAndView("note/NoteQRCode");

		String imageBase64QRCode = QRcodeUtil.getLogoQRCode(domain + "/weixin/note/reader/read/" + noteId, saveQrCodePicPath);
		
		Note note = noteService.findById(noteId);
		
		note.setNote_stock_code(StringHelper.hide(note.getNote_stock_code(), 3, 0));
		
		User user = userService.findById(note.getNote_user_id());
		
		//String shareUrl = domain + "/weixin/note/qrcode/" + note.getNote_id();
		
		//笔记信息
		mav.addObject("note", note);
		// 笔记作者的用户信息
		mav.addObject("user", user);
		
		mav.addObject("imageBase64QRCode", imageBase64QRCode);
		//二维码分享
		//mav.addObject("shareUrl", shareUrl);
		return mav;
	}

	/**
	 * 关注
	 */
	@RequestMapping(value = "focus", method = RequestMethod.GET)
	public String focus(@RequestParam(value = "t", required = true) int type, @RequestParam(value = "userId", required = true) String userId) throws Exception {
		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(new ModelAndView());
		if (null == user) {
			return null;
		}
		try {
			if (0 == type) {
				focus = new Focus();
				focus.setFocus_user_id(user.getUser_id());
				focus.setFocus_other_id(Integer.valueOf(userId));
				focusService.save(focus);
			}
			if (1 == type) {
				focus = focusService.findFocusByUserIdAndFocusId(user.getUser_id(), Integer.valueOf(userId));
				if (null != focus) {
					focusService.delete(focus);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}

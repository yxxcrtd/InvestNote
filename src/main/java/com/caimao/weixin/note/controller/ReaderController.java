package com.caimao.weixin.note.controller;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.domain.Ticker;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.domain.WxShareInfo;
import com.caimao.weixin.note.enums.ENotePayStatus;
import com.caimao.weixin.note.util.Pager;
import com.caimao.weixin.note.util.StockUtil;
import com.caimao.weixin.note.util.StringHelper;

@RestController
@RequestMapping("/weixin/note/reader")
public class ReaderController extends BaseController {

	/**
	 * 我的阅读页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/my_read", method = RequestMethod.GET)
	public ModelAndView my(@RequestParam(value = "p", required = false, defaultValue = "1") Integer p) throws Exception {
		ModelAndView mav = new ModelAndView("reader/ReaderList");
		User user = this.baseAuthInfo(mav);
		if (user == null)
			return null;

		// 分页
		String where = "r.reader_user_id = " + user.getUser_id();
		Pager pager = new Pager();
		pager.setPageNo(p);
		Reader reader = new Reader();
		Integer count = this.readerService.findAllCount(reader, "reader_user_id = " + user.getUser_id());
		pager.setTotalCount(count);
		mav.addObject("count", pager.getTotalPageCount());
		mav.addObject("list", readerService.findListByLeftJoin(pager, where));
		mav.addObject("p", p);
		// 分享信息
		setShareInfo(mav, user);
		return mav;
	}

	/**
	 * 我的阅读列表的分页
	 */
	@RequestMapping(value = "my_read/ajax", method = RequestMethod.GET)
	public String ajax(@RequestParam(value = "p", required = true, defaultValue = "1") int p) throws Exception {
		ModelAndView mav = new ModelAndView();
		User user = this.baseAuthInfo(mav);
		if (user == null)
			return null;

		String where = "r.reader_user_id = " + user.getUser_id();
		Pager pager = new Pager();
		pager.setPageNo(p);
		Reader reader = new Reader();
		int count = this.readerService.findAllCount(reader, "reader_user_id = " + user.getUser_id());
		pager.setTotalCount(count);
		JSONObject obj = new JSONObject();
		obj.put("list", readerService.findListByLeftJoin(pager, where));
		obj.put("p", p);
		return obj.toString();
	}

	/**
	 * 预览页面
	 * 
	 * @param noteId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/{id:[\\d]+}")
	public ModelAndView view(@PathVariable(value = "id") int noteId) throws Exception {
		ModelAndView mav = new ModelAndView("reader/NoteView");
		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (user == null)
			return null;

		Note note = noteService.findById(noteId);
		String originalCode = note.getNote_stock_code();
		note.setNote_stock_code(StringHelper.hide(note.getNote_stock_code(), 3, 0));
		mav.addObject("note", note);
		// 笔记作者的用户信息
		User noteUser = this.userService.findById(note.getNote_user_id());
		mav.addObject("noteUser", noteUser);

		// 如果这个没有支付，其他用户跳到别的地方，作者跳到支付页面
		if (note.getNote_pay_status() != ENotePayStatus.OK.getCode()) {
			if (note.getNote_user_id() != user.getUser_id()) {
				return new ModelAndView("redirect:/weixin/note/my");
			} else {
				return new ModelAndView("redirect:/weixin/note/pay/pay?noteId=" + note.getNote_id() + "&optType=1");
			}
		}

		// 当前登录用户如果已经购买，跳转到阅读页面
		List<Reader> readerList = readerService.findReaderByNoteIdAndUserId(note.getNote_id(), user.getUser_id());
		if (readerList != null && readerList.size() > 0) {
			return new ModelAndView("redirect:/weixin/note/reader/read/" + note.getNote_id());
		}
		// 如果阅读费为0，自动购买打开
		// if (note.getNote_open_money() <= 0.00) {
		// return new
		// ModelAndView("redirect:/weixin/note/pay/yuePay?noteId="+note.getNote_id()+"&optType=2");
		// }

		// 阅读过笔记的用户
		mav.addObject("readerList", readerService.findReaderList("r.reader_note_id = " + note.getNote_id()));
		//当前笔记有多少状态是1的=赞人数
    	mav.addObject("paise", readerService.findAllCount(new Reader(), "reader_note_id=" + noteId + " and  reader_step_paise_status=" + 1));
		//当前笔记有多少状态是2的=踩人数
    	mav.addObject("step", readerService.findAllCount(new Reader(), "reader_note_id=" + noteId + " and  reader_step_paise_status=" + 2));
    	if (readerList != null && readerList.size() > 0) {
    		if(readerList.get(0).getReader_step_paise_status() == 1) {
    			mav.addObject("type", 1);
    		}else if(readerList.get(0).getReader_step_paise_status() == 2) {
    			mav.addObject("type", 2);
    		}else {
    			mav.addObject("type", 0);
    		}
    	}
    	
		WxShareInfo shareInfo = new WxShareInfo();
		shareInfo.setTitle(noteUser.getUser_nickname() + "写了一篇投资笔记");
		if (note != null && !"".equals(note.getNote_title())) {
			shareInfo.setDesc("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，" + note.getNote_title());
			shareInfo.setFriend("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，" + note.getNote_title());
		} else {
			shareInfo.setDesc("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%");
			shareInfo.setFriend("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%");
		}
		shareInfo.setImgUrl(noteUser.getUser_header_img());
		shareInfo.setLink(this.domain + "/weixin/note/reader/view/" + note.getNote_id());
		shareInfo.setQqlink(domain + "/weixin/note/qrcode/" + note.getNote_id());
		mav.addObject("shareInfo", shareInfo);

		this.calaStatus(mav, note, originalCode);

		if (2 == note.getNote_status()) {
			mav.setViewName("reader/NoteRead");
		}
		return mav;
	}

	/**
	 * 阅读页面
	 * 
	 * @param noteId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/read/{id:[\\d]+}")
	public ModelAndView read(@PathVariable(value = "id") int noteId, @RequestParam(value = "pay", required = false, defaultValue = "0") Integer pay) throws Exception {
		ModelAndView mav = new ModelAndView("/reader/NoteRead");
		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (user == null)
			return null;

		Note note = noteService.findById(noteId);
		// 如果没有找到笔记（可能之前没有支付然后删除了）的话，直接跳转到我的主页
		if (null == note) {
			return new ModelAndView("redirect:/weixin/note/my");
		}
		String originalCode = note.getNote_stock_code();

		mav.addObject("note", note);
		// 笔记作者的用户信息
		User noteUser = this.userService.findById(note.getNote_user_id());
		mav.addObject("noteUser", noteUser);

		// 如果这个没有支付，其他用户跳到别的地方，作者跳到支付页面
		if (note.getNote_pay_status() != ENotePayStatus.OK.getCode()) {
			if (note.getNote_user_id() != user.getUser_id()) {
				return new ModelAndView("redirect:/weixin/note/my");
			} else {
				return new ModelAndView("redirect:/weixin/note/pay/pay?noteId=" + note.getNote_id() + "&optType=1");
			}
		}

		// 当前登录用户如果没有购买，跳转到阅读页面
		if (2 > note.getNote_status()) {
			List<Reader> readerList = readerService.findReaderByNoteIdAndUserId(note.getNote_id(), user.getUser_id());
			if (user.getUser_id() != note.getNote_user_id() && (readerList == null || readerList.size() <= 0)) {
				return new ModelAndView("redirect:/weixin/note/reader/view/" + note.getNote_id());
			}
		}

		// 阅读过笔记的用户
		mav.addObject("readerList", readerService.findReaderList("r.reader_note_id = " + note.getNote_id()));
		//赞过笔记的用户(1)=赞人数
    	mav.addObject("paise", readerService.findAllCount(new Reader(), "reader_note_id=" + noteId + " and  reader_step_paise_status=" + 1));
		//踩过笔记的用户(2)=踩人数
    	mav.addObject("step", readerService.findAllCount(new Reader(), "reader_note_id=" + noteId + " and  reader_step_paise_status=" + 2));
    	List<Reader> readerList = readerService.findReaderByNoteIdAndUserId(note.getNote_id(), user.getUser_id());
    	if(readerList!=null && readerList.size() > 0) {
    		if(readerList.get(0).getReader_step_paise_status() == 1) {
    			mav.addObject("type", 1);
    		}else if(readerList.get(0).getReader_step_paise_status() == 2) {
    			mav.addObject("type", 2);
    		}else {
    			mav.addObject("type", 0);
    		}
		} else {
			mav.addObject("type", -1);
    	}
		mav.addObject("pay", pay);

		WxShareInfo shareInfo = new WxShareInfo();
		shareInfo.setTitle(noteUser.getUser_nickname() + "写了一篇投资笔记");
		DecimalFormat df = new DecimalFormat("#");
		if (note != null && !"".equals(note.getNote_title())) {
			shareInfo.setDesc("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，价值" + df.format(note.getNote_open_money()) + "元" + "，" + note.getNote_title());
			shareInfo.setFriend("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，" + df.format(note.getNote_open_money()) + "元可查看" + "，" + note.getNote_title());
		} else {
			shareInfo.setDesc("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，价值" + df.format(note.getNote_open_money()) + "元");
			shareInfo.setFriend("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，" + df.format(note.getNote_open_money()) + "元可查看");
		}
		shareInfo.setImgUrl(noteUser.getUser_header_img());
		shareInfo.setQqlink(domain + "/weixin/note/qrcode/" + note.getNote_id());
		shareInfo.setLink(this.domain + "/weixin/note/reader/view/" + note.getNote_id());
		mav.addObject("shareInfo", shareInfo);

		this.calaStatus(mav, note, originalCode);
		return mav;
	}
	/**
	 * 显示阅读者头像页面
	 * 
	 * @param noteId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/initReader/{id:[\\d]+}")
	public ModelAndView initReader(@PathVariable(value = "id") int noteId) throws Exception {
		ModelAndView mav = new ModelAndView("/reader/UserHeadShow");
		List<Reader> readerList = readerService.findReaderList("r.reader_note_id = " + noteId);
		if(readerList!=null && readerList.size() > 20) {
			mav.addObject("reader_count", readerList.size());
		}
		// 阅读过笔记的用户
		mav.addObject("readerList", readerList);
		return mav;
	}

	// 计算当前价、开始价、目标价
	private void calaStatus(ModelAndView mav, Note note, String originalCode) throws Exception {
		Ticker ticker = StockUtil.getTicket(originalCode);
		if (null != ticker) {
			mav.addObject("currentPrice", ticker.getCurrentPrice());
			mav.addObject("startPrice", note.getNote_stock_open_price());
			mav.addObject("targetPrice", note.getNote_stock_open_price() * note.getNote_increase() / 100 + note.getNote_stock_open_price());
		}
	}

	
	/**
	 * 根据前台踩赞转换对DB进行更新(异步更新)
	 * step_paise_type。0:取消踩或赞，1:赞，2:踩
	 * @param step_paise_type
	 * @throws Exception 
	 */
	@RequestMapping(value = "reCounting/{id:[\\d]+}/{type:[\\d]+}", method = RequestMethod.GET)
	public String reCounting(@PathVariable(value = "id") int noteId, @PathVariable(value = "type") int step_paise_type) throws Exception {
		ModelAndView mav = new ModelAndView("/reader/NoteRead");
		// 每个页面都需要加的授权信息
		User user = baseAuthInfo(mav);
		if (user == null)
			return null;
		//只要前台的type和数据库的status不一样就可以update
		List<Reader> listReader  = readerService.findReaderByNoteIdAndUserId(noteId, user.getUser_id());
		if(listReader!=null && listReader.size() > 0) {
			if((step_paise_type != listReader.get(0).getReader_step_paise_status())) {
				Reader reader = listReader.get(0);
				reader.setReader_step_paise_status(step_paise_type);
				readerService.changeReaderTargetStatus(reader);
			}
		}
		JSONObject obj = new JSONObject();
		//赞过笔记的用户(1)=赞人数
		obj.put("paise", readerService.findAllCount(new Reader(), "reader_note_id=" + noteId + " and  reader_step_paise_status=" + 1));
		//踩过笔记的用户(2)=踩人数
		obj.put("step", readerService.findAllCount(new Reader(), "reader_note_id=" + noteId + " and  reader_step_paise_status=" + 2));
		return obj.toString();
	}
}

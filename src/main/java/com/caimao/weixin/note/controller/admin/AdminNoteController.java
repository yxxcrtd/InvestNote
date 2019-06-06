package com.caimao.weixin.note.controller.admin;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.domain.Ticker;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.util.PageUtils;
import com.caimao.weixin.note.util.Pager;
import com.caimao.weixin.note.util.StockUtil;

/**
 * 投资笔记中的笔记
 */
@RestController
@RequestMapping(value = "/weixin/note/admin/note")
public class AdminNoteController extends AdminController {

    @Autowired
    private NoteDao noteDao;

    @Value("${wx.domain}")
    protected String domain;

    /**
     * 笔记列表
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(
            @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(value = "nickName", required = false, defaultValue = "") String nickName,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
            @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer p
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/note/list");
        if (!this.checkLogin()) return null;

        String where = " 1=1 ";
        if (userId != null && !Objects.equals(userId, "")) where += " AND u.user_id = '"+userId+"'";
		if (nickName != null && !Objects.equals(nickName, ""))
			where += " AND u.user_nickname LIKE '%" + nickName + "%'";
		if (code != null && !Objects.equals(code, ""))
			where += " AND n.note_stock_code = '" + code + "'";
		if (startDate != null && !Objects.equals(startDate, ""))
			where += " AND n.note_create_time >= '" + startDate + " 00:00:00'";
		if (endDate != null && !Objects.equals(endDate, ""))
			where += " AND n.note_create_time <= '" + endDate + " 23:59:59'";

        
        Pager pager = new Pager();
        pager.setPageNo(p);
        pager.setPageSize(100);
        Integer count = this.noteDao.findAdminNoteListCount(where);
        pager.setTotalCount(count);

        List<Note> noteList = this.noteDao.findAdminNoteListPage(pager, where, "n.note_id DESC");
        if (noteList != null) {
			PageUtils pageUtils = new PageUtils(p, pager.getPageSize(), count, String.format("/weixin/note/admin/note/list?userId=%s&nickName=%s&code=%s&startDate=%s&endDate=%s&p=", userId, nickName, code, startDate, endDate));
            mav.addObject("pageHtml", pageUtils.show());
            for(Note note : noteList) {
            	                                     //未达标，笔记结束到期(取结束当天的收盘价)    //未达标，笔记未结束(取当天收盘价)
            	if(note.getNote_target_status() == 0 && (note.getNote_status() == 2 || note.getNote_status() == 1)) {
            		Ticker ticker = StockUtil.getTicket(note.getNote_stock_code());         
            		note.setNote_earnest_money(Double.parseDouble(ticker.getClosePrice()));
            	}
            }
        }
        mav.addObject("noteList", noteList);
        mav.addObject("nickName", nickName);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("domain", domain);
        return mav;
    }

    /**
     * 笔记详情
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView info(
            @RequestParam(value = "noteId") Integer noteId
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/note/info");
        if (!this.checkLogin()) return null;

        Note note = this.noteService.findById(noteId);
        if (note == null) return new ModelAndView("redirect:/weixin/note/admin/note/list");

        User user = this.userService.findById(note.getNote_user_id());
        mav.addObject("user", user);
        mav.addObject("note", note);
        return mav;
    }

    /** 获取阅读的人 */
    @RequestMapping(value = "getUsers", method = RequestMethod.GET)
    public String getUsers(@RequestParam(value = "noteId", required = true) int noteId) throws Exception {
        List<Reader> readerList = readerService.findReaderList("r.reader_note_id = " + noteId);
        JSONObject obj = new JSONObject();
        obj.put("list", readerList);
        return obj.toString();
    }

}

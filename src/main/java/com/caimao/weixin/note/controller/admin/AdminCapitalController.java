package com.caimao.weixin.note.controller.admin;

import com.caimao.weixin.note.dao.CapitalDao;
import com.caimao.weixin.note.dao.DepositDao;
import com.caimao.weixin.note.dao.NoteDao;
import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.dao.WithdrawDao;
import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.util.PageUtils;
import com.caimao.weixin.note.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

/**
 * 资金流水
 */
@Controller
@RequestMapping(value = "/weixin/note/admin/capital")
public class AdminCapitalController extends AdminController {

    @Autowired
    private CapitalDao capitalDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private DepositDao depositDao;
    
    @Autowired
    private NoteDao noteDao;
    
    @Autowired
    private WithdrawDao withdrawDao;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(
            @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(value = "nickname", required = false, defaultValue = "") String nickname,
            @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
            @RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer p
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/capital/list");
        if (!this.checkLogin()) return null;

        String where = " 1=1 ";
        if (userId != null && !Objects.equals(userId, "")) where += " AND u.user_id = '"+userId+"' ";
        if (nickname != null && !Objects.equals(nickname, "")) where += " AND u.`user_nickname` LIKE '%"+nickname+"%'";
        if (type != null && !Objects.equals(type, "")) where += " AND c.`capital_type` = '"+type+"'";
        if (startDate != null && !Objects.equals(startDate, "")) where += " AND c.`capital_create_time` >= '"+startDate+" 00:00:00'";
        if (endDate != null && !Objects.equals(endDate, "")) where += " AND c.`capital_create_time` <= '"+endDate+" 23:59:59'";

        Pager pager = new Pager();
        pager.setPageNo(p);
        pager.setPageSize(100);
        Integer count = this.capitalDao.findAdminListCount(where);
        pager.setTotalCount(count);

        List<Capital> capitalList = this.capitalDao.findAdminListPage(pager, where, "c.capital_id DESC");
        if (capitalList != null) {
            PageUtils pageUtils = new PageUtils(p, pager.getPageSize(), count, String.format("/weixin/note/admin/capital/list?userId=%s&nickname=%s&startDate=%s&endDate=%s&type=%s&p=", userId, nickname, startDate, endDate, type));
            mav.addObject("pageHtml", pageUtils.show());
        }
        mav.addObject("list", capitalList);
        mav.addObject("nickname", nickname);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("type", type);
        return mav;
    }
    /**
     * 按日统计每日充值提现记录
     * @return
     */
    @RequestMapping(value = "/day_total", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView capitalDayTotal(
            @RequestParam(value = "type", required = false, defaultValue = "1") Integer type
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/capital/day_total");
        if (!this.checkLogin()) return null;
        if (type.equals(1)) {
            // 充值记录
            mav.addObject("list", this.depositDao.findAdminDepositDay());
        } else if(type.equals(2)){
            // 提现记录
            mav.addObject("list", this.withdrawDao.findAdminWithdrawDay());
        } else if(type.equals(3)) {
        	//总可用金额
        	mav.addObject("list", this.userDao.findAdminTotalAmountDay());
        } else if(type.equals(4)) {
        	//总阅读费冻结
        	mav.addObject("list", this.noteDao.findAdminTotalReadingDay());
        } else if(type.equals(5)) {
        	//总发红包冻结
        	mav.addObject("list", this.noteDao.findAdminTotalPacketsDay());
        }
        mav.addObject("type", type);
        return mav;
    }


}

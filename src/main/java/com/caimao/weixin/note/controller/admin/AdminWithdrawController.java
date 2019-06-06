package com.caimao.weixin.note.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.caimao.weixin.note.dao.WithdrawDao;
import com.caimao.weixin.note.domain.Withdraw;
import com.caimao.weixin.note.util.PageUtils;
import com.caimao.weixin.note.util.Pager;

/**
 * 后台提现管理
 */
@RestController
@RequestMapping(value = "/weixin/note/admin/withdraw")
public class AdminWithdrawController extends AdminController {

    @Autowired
    private WithdrawDao withdrawDao;

    /**
     * 提现列表
     */
    @RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam(value = "userId", required = false) Integer userId, @RequestParam(value = "status", required = false) Integer status, @RequestParam(value = "p", required = false, defaultValue = "1") Integer p) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/withdraw/list");
        if (!this.checkLogin()) return null;

        String where = " 1=1 ";
        if (userId != null && userId != 0) where += " AND u.user_id = " + userId;
        if (status != null && status != 0) where += " AND w.withdraw_status = " + status;

        Pager pager = new Pager();
        pager.setPageNo(p);
        pager.setPageSize(100);
        Integer count = this.withdrawDao.findAdminWithdrawListCount(where);
        pager.setTotalCount(count);

        List<Withdraw> withdrawList = this.withdrawDao.findAdminWithdrawList(pager, where, "w.withdraw_id DESC");
		if (null != withdrawList && !withdrawList.isEmpty()) {
            PageUtils pageUtils = new PageUtils(p, pager.getPageSize(), count, String.format("/weixin/note/admin/withdraw/list?userId=%s&status=%s&p=", userId, status));
            mav.addObject("pageHtml", pageUtils.show());
        }
        mav.addObject("list", withdrawList);
        mav.addObject("userId", userId);
        mav.addObject("status", status);
        return mav;
    }

    /**
     * 处理提现
     */
    @ResponseBody
    @RequestMapping(value = "/do")
	public Map<String, Boolean> doWithdraw(@RequestParam(value = "wId") Integer wId, @RequestParam(value = "status") Integer status) throws Exception {
        if (!this.checkLogin()) return null;
        Withdraw withdraw = this.withdrawDao.findById(wId);
        Boolean res = this.withdrawService.doWithdraw(withdraw, status);
        Map<String, Boolean> ret = new HashMap<>();
        ret.put("res", res);
        return ret;
    }

    /**
     * 处理提现
     */
    @ResponseBody
    @RequestMapping(value = "/doCheck")
	public Map<String, Boolean> doCheck(@RequestParam(value = "wId") Integer wId) throws Exception {
        if (!this.checkLogin()) return null;
        Boolean res = this.withdrawService.checkWithdraw(wId);
        Map<String, Boolean> ret = new HashMap<>();
        ret.put("res", res);
        return ret;
    }

}

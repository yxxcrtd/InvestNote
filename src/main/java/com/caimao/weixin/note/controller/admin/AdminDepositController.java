package com.caimao.weixin.note.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.caimao.weixin.note.util.PageUtils;
import com.caimao.weixin.note.util.Pager;

/**
 * 充值明细
 */
@Controller
@RequestMapping(value = "/weixin/note/admin/deposit")
public class AdminDepositController extends AdminController {

    /**
     * 充值列表
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(
            @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
            @RequestParam(value = "nickName", required = false, defaultValue = "") String nickName,
            @RequestParam(value = "billNo", required = false, defaultValue = "") String billNo,
            @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
            @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer p
    ) {
        ModelAndView mav = new ModelAndView("/admin/deposit/list");

        Pager pager = new Pager();
        pager.setPageNo(p);
        pager.setPageSize(100);
		List<Map<String, Object>> list = this.depositService.findAdminDepositList(pager, userId, nickName, billNo, startDate, endDate, status);
        if (list != null) {
            PageUtils pageUtils = new PageUtils(p, pager.getPageSize(), pager.getTotalCount(), String.format("/weixin/note/admin/deposit/list?userId=%s&nickName=%s&startDate=%s&endDate=%s&status=%s&p=", userId, nickName, startDate, endDate, status));
            mav.addObject("pageHtml", pageUtils.show());
        }

        mav.addObject("list", list);
        mav.addObject("nickName", nickName);
        mav.addObject("billNo", billNo);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("status", status);
        return mav;
    }

}

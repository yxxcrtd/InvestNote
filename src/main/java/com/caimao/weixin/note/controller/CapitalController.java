package com.caimao.weixin.note.controller;

import com.alibaba.fastjson.JSONObject;
import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.domain.Withdraw;
import com.caimao.weixin.note.util.Pager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/weixin/note/capital")
public class CapitalController extends BaseController {

    /**
     * 资金流水首页
     */
    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public ModelAndView index(
            @RequestParam(value = "p", required = true, defaultValue = "1") int p,
            @RequestParam(value = "t", required = false) Integer t
    ) throws Exception {
        ModelAndView mav = new ModelAndView("capital/CapitalList");
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;

        String where = "capital_user_id = " + user.getUser_id();
        if (t != null) {
            where += " AND capital_type = " + t;
        }
        Pager pager = new Pager();
        pager.setPageNo(p);
        int count = capitalService.findAllCount(capital, where);
        pager.setTotalCount(count);
        mav.addObject("list", capitalService.findByPager(pager, where, "capital_id desc, capital_create_time desc"));
        mav.addObject("count", pager.getTotalPageCount());
        mav.addObject("p", p);
        mav.addObject("t", t);


        // 获取未处理完成的提现记录
        Pager pager1 = new Pager();
        pager1.setPageNo(1);
        pager1.setTotalCount(10);
        List<Withdraw> withdrawList = this.withdrawService.findByPager(pager1, "withdraw_user_id = '"+user.getUser_id()+"' AND withdraw_status IN(1, 2)", "withdraw_id DESC");
        mav.addObject("withdrawList", withdrawList);

        return mav;
    }

    /**
     * 资金流水的分页
     */
    @RequestMapping(value = "ajax", method = RequestMethod.GET)
    public String ajax(@RequestParam(value = "p", required = true, defaultValue = "1") int p, @RequestParam(value = "t", required = false) Integer t) throws Exception {
        ModelAndView mav = new ModelAndView();
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;

        String where = "capital_user_id = " + user.getUser_id();
        if (t != null) {
            where += " AND capital_type = " + t;
        }
        Pager pager = new Pager();
        pager.setPageNo(p);
        int count = capitalService.findAllCount(capital, where);
        pager.setTotalCount(count);
        List<Capital> list = capitalService.findByPager(pager, where, "capital_id desc, capital_create_time desc");
        JSONObject obj = new JSONObject();
        obj.put("list", list);
        obj.put("t", t);
        obj.put("p", p);
        return obj.toString();
    }

}

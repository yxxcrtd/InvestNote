package com.caimao.weixin.note.controller.admin;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.caimao.weixin.note.dao.BankDao;
import com.caimao.weixin.note.domain.Bank;
import com.caimao.weixin.note.domain.Reader;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.enums.EBankStatus;
import com.caimao.weixin.note.util.PageUtils;
import com.caimao.weixin.note.util.Pager;

/**
 * 投资笔记用户后台控制器
 */
@Controller
@RequestMapping(value = "/weixin/note/admin/user")
public class AdminUserController extends AdminController {

    @Autowired
    private BankDao bankDao;

    /**
     * 用户列表
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView userList(
            @RequestParam(value = "nickName", required = false, defaultValue = "") String nickName,
            @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer p
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/user/list");
        if (!this.checkLogin()) return null;

        String where = " 1=1 ";
        if (nickName != null && !Objects.equals(nickName, "")) where += " AND user_nickname LIKE '%"+nickName+"%'";
        if (startDate != null && !Objects.equals(startDate, "")) where += " AND user_create_time >= '"+startDate+" 00:00:00'";
        if (endDate != null && !Objects.equals(endDate, "")) where += " AND user_create_time <= '"+endDate+" 23:59:59'";
        Integer count = this.userService.findAllCount(new User(), where);
        Pager pager = new Pager();
        pager.setPageNo(p);
        pager.setPageSize(100);
        pager.setTotalCount(count);
        List<User> userList = this.userService.findByPager(pager, where, "user_id DESC");
        mav.addObject("userList", userList);
        Map<String, Object> userReadCount = new TreeMap<>();
        if (userList != null) {
            PageUtils pageUtils = new PageUtils(p, pager.getPageSize(), count, String.format("/weixin/note/admin/user/list?nickName=%s&startDate=%s&endDate=%s&p=", nickName, startDate, endDate));
            mav.addObject("pageHtml", pageUtils.show());
            for (User user : userList) {
            	userReadCount.put(String.valueOf(user.getUser_id()), this.readerService.findAllCount(new Reader(), "reader_user_id = '"+user.getUser_id()+"'" ));
      /*          if(user.getUser_id() == 2) {
                	String selectusableMoney = "SUM(capital_mount)";
                	String tablecapital = "capital";
                	//每个用户阅读总额
                	String whereEveryOne = "1=1  AND capital_type = 6 AND capital_user_id =  " + user.getUser_id() + " GROUP BY capital_user_id " ;
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereEveryOne));
                	System.out.println("每个用户阅读总额" + this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereEveryOne));
                	String whereAll = "1=1 AND capital_type = 6";
                	//所有用户阅读总额
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereAll));
                	System.out.println("所有用户阅读总额" + this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereAll));
                }*/
/*                if(user.getUser_id() == 2) {
                	String selectusableMoney = "SUM(capital_mount)";
                	String tablecapital = "capital";
                	//每个用户写笔记总额
                	String whereEveryOne = "1=1  AND capital_type = 3 AND capital_user_id =  " + user.getUser_id() + " GROUP BY capital_user_id " ;
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereEveryOne));
                	System.out.println("每个用户写笔记总额" + this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereEveryOne));
                	String whereAll = "1=1 AND capital_type = 3";
                	//所有用户写笔记总额
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereAll));
                	System.out.println("所有用户写笔记总额" + this.depositService.findcountByCondition(selectusableMoney, tablecapital, whereAll));
                }
                if(user.getUser_id() == 9267) {
                   	String selectusableMoney = "SUM(user_available_money)";
                	String tableUser = "user";
                	//每个用户可用余额
                	String whereEveryOne = "1=1  AND user_id =  " + user.getUser_id();
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectusableMoney, tableUser, whereEveryOne));
                	System.out.println("每个用户可用余额" + this.depositService.findcountByCondition(selectusableMoney, tableUser, whereEveryOne));
                	String whereAll = "1=1";
                	//所有用户可用余额
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectusableMoney, tableUser, whereAll));
                	System.out.println("所有用户可用余额" + this.depositService.findcountByCondition(selectusableMoney, tableUser, whereAll));
                }
                if(user.getUser_id() == 9069) {
                	String selectDepositMoney = "SUM(deposit_money)";
                	String tableDeposit = "deposit";
                	String whereEveryOne = "1=1  AND deposit_status = '2'  AND deposit_user_id = " + user.getUser_id() + "  GROUP BY deposit_user_id";
                	//每个用户充值总额
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectDepositMoney, tableDeposit, whereEveryOne));
                	System.out.println("每个用户充值总金额" + this.depositService.findcountByCondition(selectDepositMoney, tableDeposit, whereEveryOne));
                	String whereAll = "1=1  AND deposit_status = '2' ";
                	//所有用户充值总额
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectDepositMoney, tableDeposit, whereAll));
                	System.out.println("所有用户充值总金额" + this.depositService.findcountByCondition(selectDepositMoney, tableDeposit, whereAll));
                }
                if(user.getUser_id() == 674) {
                	String selectWithdrawMoney = "SUM(withdraw_money)";
                	String tableWithdraw = "withdraw";
                	//每个用户提现总额
                	String whereEveryOne = "1=1 AND  withdraw_status = '3'  AND withdraw_user_id = " + user.getUser_id() + " GROUP BY withdraw_user_id ";
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectWithdrawMoney, tableWithdraw, whereEveryOne));
                	System.out.println("每个用户提现总金额" + this.depositService.findcountByCondition(selectWithdrawMoney, tableWithdraw, whereEveryOne));
                	String whereAll = "1=1 AND  withdraw_status = '3'";
                	//所有用户提现总额
                	userReadCount.put(("totalRecharge"+user.getUser_id()).trim(), this.depositService.findcountByCondition(selectWithdrawMoney, tableWithdraw, whereAll));
                	System.out.println("所有用户提现总金额" + this.depositService.findcountByCondition(selectWithdrawMoney, tableWithdraw, whereAll));
                }*/
                //userReadCount.put("reflectRecharge", this.depositService.findSumMoneyBy(user.getUser_id(), 1));
            }
        }
        mav.addObject("userReadCount", userReadCount);
        mav.addObject("nickName", nickName);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);

        return mav;
    }

    /**
     * 用户信息
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
	public ModelAndView userInfo(@RequestParam(value = "userId") Integer userId) throws Exception {
        ModelAndView mav = new ModelAndView("/admin/user/info");
        if (!this.checkLogin()) return null;

        User user = this.userService.findById(userId);
        if (user == null) return new ModelAndView("redirect:/weixin/note/admin/user/list");

        mav.addObject("user", user);
        // 获取绑定银行卡
        Bank bank = this.bankDao.findByUserIdAndStatus(userId, EBankStatus.BIND.getCode());
        mav.addObject("bank", bank);

        return mav;
    }
    
	@RequestMapping("excel")
	public String excel(ModelMap modelMap) {
		//Integer count = userService.findAllCount(new User(), "");
		//Pager pager = new Pager();
		//pager.setPageNo(1);
		//pager.setPageSize(count);
		//pager.setTotalCount(count);
		//List<User> userList = userService.findByPager(pager, "", "user_id DESC");
		//modelMap.put("userList", userList);
		return "userListExcel";
	}

}

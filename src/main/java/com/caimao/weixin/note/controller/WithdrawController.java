package com.caimao.weixin.note.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.caimao.weixin.note.dao.BankDao;
import com.caimao.weixin.note.domain.Bank;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.domain.Withdraw;
import com.caimao.weixin.note.enums.EBankStatus;
import com.caimao.weixin.note.util.StringHelper;
import com.caimao.weixin.note.util.execption.BusinessException;
import com.caimao.weixin.note.util.heepay.HeepayEntity.BatchResEntity;

/**
 * 提现控制器
 */
@Controller
@RequestMapping(value = "/weixin/note/withdraw")
public class WithdrawController extends BaseController {

    @Autowired
    private BankDao bankDao;

    /**
     * 绑定银行卡的页面
     * @return
     */
    @RequestMapping(value = "/bindBank", method = RequestMethod.GET)
    public ModelAndView bindBank() throws Exception {
        ModelAndView mav = new ModelAndView("/withdraw/bindBank");
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;

        // 查询用户是否有绑定银行卡，有则跳转到提现页面
        Bank bank = this.bankDao.findByUserIdAndStatus(user.getUser_id(), EBankStatus.BIND.getCode());
        if (bank != null) {
            return new ModelAndView("redirect:/weixin/note/withdraw/index");
        }

        return mav;
    }

    /**
     * 绑定银行卡操作
     * @return
     */
    @RequestMapping(value = "/bindBank", method = {RequestMethod.POST})
    public ModelAndView doBindBank(
            @ModelAttribute("obj") @Valid Bank bank, BindingResult result,
            @RequestParam(value = "smsCode") String smsCode
    ) throws Exception {
        ModelAndView mav = new ModelAndView("redirect:/weixin/note/withdraw/index");
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;
        // 参数检查
        if (result.hasErrors()) {
            throw new BusinessException(result.getAllErrors().get(0).getDefaultMessage(), 888888);
        }

        // 验证手机短信验证码
        if (!this.smsService.verifySmsCode(user.getUser_id(), bank.getBank_phone(), smsCode)) {
            throw new BusinessException("短信验证码错误", 888888);
        }

        bank.setBank_user_id(user.getUser_id());
        this.bankService.bindBank(bank);

        return mav;
    }

    /**
     * 解绑银行卡
     * @param bankId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unBindBank", method = {RequestMethod.GET})
    public ModelAndView unBindBank(
            @RequestParam(value = "bankId") Integer bankId
    ) throws Exception {
        ModelAndView mav = new ModelAndView("redirect:/weixin/note/withdraw/bindBank");
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;

        // 解绑银行卡
        this.bankService.unBindBank(user.getUser_id(), bankId);
        return mav;
    }

    /**
     * 提现的页面
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView("/withdraw/index");
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;

        // 查询用户绑定的银行卡，没有则跳转到绑定银行卡的页面
        Bank bank = this.bankDao.findByUserIdAndStatus(user.getUser_id(), EBankStatus.BIND.getCode());
        if (bank == null) {
            return new ModelAndView("redirect:/weixin/note/withdraw/bindBank");
        }
        bank.setBank_user_code(StringHelper.hide(bank.getBank_user_code(), 4, 4));
        mav.addObject("bank", bank);

        mav.addObject("todayWithdraw", this.withdrawService.todayWithdraw(user.getUser_id()));

        return mav;
    }

    // 用户提现请求处理
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public ModelAndView index(
            @ModelAttribute("obj") @Valid Withdraw withdraw, BindingResult result,
            @RequestParam(value = "smsCode") String smsCode,
            final RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) throws Exception {
        ModelAndView mav = new ModelAndView("redirect:/weixin/note/withdraw/index");
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;

        if (result.hasErrors()) {
            throw new BusinessException(result.getAllErrors().get(0).getDefaultMessage(), 888888);
        }

        Bank bank = this.bankDao.findByUserIdAndStatus(user.getUser_id(), EBankStatus.BIND.getCode());
        if (bank == null) {
            return new ModelAndView("redirect:/weixin/note/withdraw/bindBank");
        }
        if (!this.smsService.verifySmsCode(user.getUser_id(), user.getUser_phone(), smsCode)) {
            throw new BusinessException("短信验证码错误", 888888);
        }
        withdraw.setWithdraw_bank_id(bank.getBank_id());
        withdraw.setWithdraw_user_id(user.getUser_id());
        this.withdrawService.recordWithdraw(withdraw);

        response.sendRedirect("/weixin/note/capital/index");
        return null;
    }

    /**
     * 提现结果异步通知
     * @return
     */
    @RequestMapping(value = "/asyncNotice", method = {RequestMethod.GET, RequestMethod.POST})
    public void asyncNoticeResult(
            @RequestParam(value = "ret_code") String ret_code,
            @RequestParam(value = "ret_msg") String ret_msg,
            @RequestParam(value = "agent_id") String agent_id,
            @RequestParam(value = "hy_bill_no") String hy_bill_no,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "batch_no") String batch_no,
            @RequestParam(value = "batch_amt") String batch_amt,
            @RequestParam(value = "batch_num") String batch_num,
            @RequestParam(value = "detail_data") String detail_data,
            @RequestParam(value = "ext_param1") String ext_param1,
            @RequestParam(value = "sign") String sign,
            HttpServletResponse response
    ) throws Exception {
        BatchResEntity batchResEntity = new BatchResEntity();
        batchResEntity.setRet_code(ret_code);
        batchResEntity.setRet_msg(ret_msg);
        batchResEntity.setAgent_id(agent_id);
        batchResEntity.setHy_bill_no(hy_bill_no);
        batchResEntity.setStatus(status);
        batchResEntity.setBatch_no(batch_no);
        batchResEntity.setBatch_amt(batch_amt);
        batchResEntity.setBatch_num(batch_num);
        batchResEntity.setDetail_data(detail_data);
        batchResEntity.setExt_param1(ext_param1);
        batchResEntity.setSign(sign);
        Boolean batchResult = this.withdrawService.resWithdraw(batchResEntity);
        if (batchResult) {
            response.getWriter().print("ok");
        } else {
            response.getWriter().print("error");
        }
        response.getWriter().flush();
    }

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/send_sms", method = {RequestMethod.GET, RequestMethod.POST})
    public void sendSms(
            @RequestParam(value = "mobile", required = false) String mobile,
            HttpServletResponse response
    ) throws Exception {
        ModelAndView mav = new ModelAndView();
        User user = this.baseAuthInfo(mav);
        if (user == null) return ;

        if (user.getUser_phone() != null && !Objects.equals(user.getUser_phone(), "")) {
            mobile = user.getUser_phone();
        }

        this.smsService.sendSmsCode(user.getUser_id(), mobile);
        response.getWriter().print("ok");
        response.getWriter().flush();
    }

}

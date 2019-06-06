package com.caimao.weixin.note.controller;

import com.caimao.weixin.note.dao.DepositDao;
import com.caimao.weixin.note.domain.Deposit;
import com.caimao.weixin.note.domain.Note;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.domain.WxShareInfo;
import com.caimao.weixin.note.enums.EDepositType;
import com.caimao.weixin.note.util.StringHelper;
import com.caimao.weixin.note.util.heepay.Common.HeepayHelper;
import com.caimao.weixin.note.util.heepay.Common.Md5Tools;
import com.caimao.weixin.note.util.heepay.HeepayEntity.GatewayResEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 充值的控制器
 */
@Controller
@RequestMapping(value = "/weixin/note/pay")
public class PayController extends BaseController {

    @Value("${wx.domain}")
    private String domain;

    @Autowired
    private HeepayHelper heepayHelper;
    @Autowired
    private DepositDao depositDao;

    /**
     * 支付的页面（创建笔记、阅读笔记）
     * @param noteId
     * @param optType
     * @return
     */
    @RequestMapping(value = "/pay", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView pay(
            @RequestParam(value = "noteId") Integer noteId,
            @RequestParam(value = "optType") Integer optType
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/pay/pay");
        // 每个页面都需要加的授权信息
        User user = baseAuthInfo(mav);
        if (user == null) return null;

        // 查询笔记
        Note note = this.noteService.findById(noteId);
        if (note == null) {
            return new ModelAndView("redirect:/weixin/note/my");
        }
        Double money = note.getNote_open_money();
        if (money == 0.00) {
            return new ModelAndView("redirect:/weixin/note/pay/yuePay?noteId="+noteId+"&optType="+optType);
        }
        Boolean yuePay = false;
        if (user.getUser_available_money() > money) {
            yuePay = true;
        }
        // 如果是支付笔记保证金，判断笔记作者与登录用户是否一致
        if (optType.equals(EDepositType.NOTE.getCode()) && user.getUser_id() != note.getNote_user_id()) {
            return new ModelAndView("redirect:/weixin/note/my");
        }
        // 如果是作者本身阅读的话，直接跳转到阅读页面
        if (Objects.equals(optType, EDepositType.READ.getCode()) && user.getUser_id() == note.getNote_user_id()) {
            return new ModelAndView("redirect:/weixin/note/reader/read/"+note.getNote_id());
        }
        mav.addObject("user", user);
        mav.addObject("note", note);
        mav.addObject("yuepay", yuePay);
        mav.addObject("optType", optType);
        mav.addObject("money", money);
        return mav;
    }

    /**
     * H5方式进行充值
     * @param noteId 要充值的笔记ID
     * @param optType 充值是为了什么？1 创建笔记 2 阅读笔记
     */
    @RequestMapping(value = "/h5Pay", method = {RequestMethod.GET, RequestMethod.POST})
    public void H5PayJump(
            @RequestParam(value = "noteId") Integer noteId,
            @RequestParam(value = "optType") Integer optType,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        ModelAndView mav = new ModelAndView();
        // 每个页面都需要加的授权信息
        User user = baseAuthInfo(mav);
        if (user == null) return;
        String jumpUrl = this.depositService.getPayUrl(user.getUser_id(), noteId, optType, 1);
        // 跳转过去充值
        response.sendRedirect(jumpUrl);
    }

    /**
     * wx方式进行充值
     * @param noteId 要充值的笔记ID
     * @param optType 充值是为了什么？1 创建笔记 2 阅读笔记
     */
    @RequestMapping(value = "/wxPay", method = {RequestMethod.GET, RequestMethod.POST})
    public void WXPayJump(
            @RequestParam(value = "noteId") Integer noteId,
            @RequestParam(value = "optType") Integer optType,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        ModelAndView mav = new ModelAndView();
        // 每个页面都需要加的授权信息
        User user = baseAuthInfo(mav);
        if (user == null) return ;
        String jumpUrl = this.depositService.getPayUrl(user.getUser_id(), noteId, optType, 2);
        // 跳转过去充值
        response.sendRedirect(jumpUrl);
    }

    /**
     * 充值成功后同步返回的成功页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/payResult", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView payResult(
            GatewayResEntity gatewayResEntity,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/pay/paySuccess");
        // 每个页面都需要加的授权信息
        User user = baseAuthInfo(mav);
        if (user == null) return null;

        Note note = new Note();
        // 验证返回值
        Boolean verifiRes = this.heepayHelper.VerifiSign(gatewayResEntity);
        mav.addObject("verifiRes", verifiRes);
        mav.addObject("payResult", gatewayResEntity.getResult());
        if (verifiRes) {
            // 签名成功，处理数据表中的订单状态，并做相应的处理
            String result = gatewayResEntity.getResult();
            String billId = gatewayResEntity.getAgent_bill_id();
            if (result.equals("1")) {
                // 获取充值订单表记录
                Deposit deposit = this.depositDao.findByBillId(billId);
                mav.addObject("deposit", deposit);
                note = this.noteService.findById(deposit.getDeposit_note_id());
                note.setNote_stock_code(StringHelper.hide(note.getNote_stock_code(), 3, 0));
                mav.addObject("note", note);

                // 根据充值的类型，进行不同的操作
                if (deposit.getDeposit_action() == EDepositType.NOTE.getCode()) {
                    // 如果不是作者本身支付笔记的话，直接跳转到阅读页面
                    if (user.getUser_id() != note.getNote_user_id()) {
                        return new ModelAndView("redirect:/weixin/note/reader/read/"+note.getNote_id()+"?pay=1");
                    }
                    // 笔记保证金支付操作
                    this.depositService.proccessNotePay(deposit, note);
                } else if (deposit.getDeposit_action() == EDepositType.READ.getCode()) {
                    // 阅读笔记支付操作
                    this.depositService.proccessReadPay(deposit);
                    // 跳转到阅读笔记的页面
                    return new ModelAndView("redirect:/weixin/note/reader/read/"+note.getNote_id()+"?pay=1");
                }
            }
        }

        User noteUser = this.userService.findById(note.getNote_user_id());

        WxShareInfo shareInfo = new WxShareInfo();
        shareInfo.setTitle(noteUser.getUser_nickname()+"写了一篇投资笔记");
        if(note!=null && !"".equals(note.getNote_title())) {
        	shareInfo.setDesc("股票代码" + StringHelper.hide
        			(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() 
        			+ "日预期收益率" + note.getNote_increase() + "%，价值" + note.getNote_title());
        	shareInfo.setFriend("股票代码" + StringHelper.hide
        			(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() 
        			+ "日预期收益率" + note.getNote_increase() + "%，"  + note.getNote_title());
        }else {
        	shareInfo.setDesc("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) 
        	+ "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%");
        	shareInfo.setFriend("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + 
        			"，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%");
        }
        shareInfo.setImgUrl(noteUser.getUser_header_img());
        shareInfo.setLink(this.domain+"/weixin/note/reader/view/"+note.getNote_id());
        mav.addObject("shareInfo", shareInfo);

        return mav;
    }

    /**
     * 使用余额支付成功的页面
     * @param noteId
     * @param optType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/yuePay", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView yuePay(
            @RequestParam(value = "noteId") Integer noteId,
            @RequestParam(value = "optType") Integer optType
    ) throws Exception {
        ModelAndView mav = new ModelAndView("/pay/paySuccess");
        User user = this.baseAuthInfo(mav);
        if (user == null) return null;
        // 查询笔记信息
        Note note = this.noteService.findById(noteId);
        // 如果是作者本身阅读的话，直接跳转到阅读页面
        if (Objects.equals(optType, EDepositType.READ.getCode()) && user.getUser_id() == note.getNote_user_id()) {
            return new ModelAndView("redirect:/weixin/note/reader/read/"+note.getNote_id()+"?pay=1");
        }
        // 如果不是作者本身支付笔记的话，直接跳转到阅读页面
        if (optType.equals(EDepositType.NOTE.getCode()) && user.getUser_id() != note.getNote_user_id()) {
            return new ModelAndView("redirect:/weixin/note/reader/read/"+note.getNote_id()+"?pay=1");
        }

        this.depositService.processYuePay(user.getUser_id(), noteId, optType, note.getNote_open_money());

        note.setNote_stock_code(StringHelper.hide(note.getNote_stock_code(), 3, 0));
        mav.addObject("note", note);
        mav.addObject("optType", optType);
        mav.addObject("verifiRes", true);
        mav.addObject("payResult", "1");
        User noteUser = this.userService.findById(note.getNote_user_id());

        WxShareInfo shareInfo = new WxShareInfo();
        shareInfo.setTitle(noteUser.getUser_nickname()+"写了一篇投资笔记");
        DecimalFormat df = new DecimalFormat("#");
        if(note!=null && !"".equals(note.getNote_title())) {
        	shareInfo.setDesc("股票代码" + StringHelper.hide
        			(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() 
        			+ "日预期收益率" + note.getNote_increase() + "%，价值" + df.format(note.getNote_open_money()) 
        			+ "元" + "，" + note.getNote_title());
        	shareInfo.setFriend("股票代码" + StringHelper.hide
        			(note.getNote_stock_code(), 3, 0) + "，" + note.getNote_target_day() 
        			+ "日预期收益率" + note.getNote_increase() + "%，" + df.format(note.getNote_open_money()) 
        			+ "元可查看" + "，" + note.getNote_title());
        }else {
        	shareInfo.setDesc("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) 
        	+ "，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，价值" + 
        			df.format(note.getNote_open_money()) + "元");
        	shareInfo.setFriend("股票代码" + StringHelper.hide(note.getNote_stock_code(), 3, 0) + 
        			"，" + note.getNote_target_day() + "日预期收益率" + note.getNote_increase() + "%，" + 
        			df.format(note.getNote_open_money()) + "元可查看");
        }
        shareInfo.setImgUrl(noteUser.getUser_header_img());
        shareInfo.setLink(this.domain+"/weixin/note/reader/view/"+note.getNote_id());
        mav.addObject("shareInfo", shareInfo);

        // 阅读直接跳转到阅读页面
        if (optType.equals(EDepositType.READ.getCode())) {
            return new ModelAndView("redirect:/weixin/note/reader/read/"+note.getNote_id());
        }
        return mav;
    }


    /**
     * 异步支付结果通知接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/asyncResult", method = {RequestMethod.GET, RequestMethod.POST})
    public String asyncResult(
            @RequestParam(value = "result") String result,
            @RequestParam(value = "pay_message") String pay_message,
            @RequestParam(value = "agent_id") String agent_id,
            @RequestParam(value = "jnet_bill_no") String jnet_bill_no,
            @RequestParam(value = "agent_bill_id") String agent_bill_id,
            @RequestParam(value = "pay_type") String pay_type,
            @RequestParam(value = "pay_amt") String pay_amt,
            @RequestParam(value = "remark") String remark,
            @RequestParam(value = "sign") String sign
    ) {
        String _str = String.format(
                "result=%s&agent_id=%s&jnet_bill_no=%s&agent_bill_id=%s&pay_type=%s&pay_amt=%s&remark=%s&key=%s",
                result, agent_id, jnet_bill_no, agent_bill_id, pay_type, pay_amt, remark, this.heepayHelper.getHeepayBatchKey()
                );
        String mySign = Md5Tools.MD5(_str).toLowerCase();
        if (mySign.equals(sign)) {
            // 签名验证成功
            if (result.equals("1")) {
                // 获取充值订单
                Deposit deposit = this.depositDao.findByBillId(agent_bill_id);
                // 获取笔记记录
                Note note = this.noteService.findById(deposit.getDeposit_note_id());

                if (deposit.getDeposit_action() == EDepositType.NOTE.getCode()) {
                    // 支付笔记保证金，更新充值记录状态，并更新笔记状态
                    this.depositService.proccessNotePay(deposit, note);
                }
                if (deposit.getDeposit_action() == EDepositType.READ.getCode()) {
                    // 支付阅读笔记费用，更新充值记录状态 ，并更新阅读记录状态
                    this.depositService.proccessReadPay(deposit);
                }
                return "ok";
            }
            return "error";
        } else {
            return "error";
        }
    }

}

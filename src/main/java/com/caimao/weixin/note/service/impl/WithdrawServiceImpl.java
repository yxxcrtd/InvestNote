package com.caimao.weixin.note.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.weixin.note.dao.BankDao;
import com.caimao.weixin.note.dao.CapitalDao;
import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.dao.WithdrawDao;
import com.caimao.weixin.note.domain.Bank;
import com.caimao.weixin.note.domain.Capital;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.domain.Withdraw;
import com.caimao.weixin.note.enums.ECapitalType;
import com.caimao.weixin.note.enums.EWithdrawStatus;
import com.caimao.weixin.note.service.WithdrawService;
import com.caimao.weixin.note.util.DateUtil;
import com.caimao.weixin.note.util.execption.BusinessException;
import com.caimao.weixin.note.util.heepay.Common.HeepayHelper;
import com.caimao.weixin.note.util.heepay.Common.Md5Tools;
import com.caimao.weixin.note.util.heepay.HeepayEntity.BatchPayEntity;
import com.caimao.weixin.note.util.heepay.HeepayEntity.BatchResEntity;

@Service
public class WithdrawServiceImpl extends BaseServiceImpl<Withdraw, Integer> implements WithdrawService {

    @Autowired
    private HeepayHelper heepayHelper;
    @Autowired
    private BankDao bankDao;
    @Autowired
    private WithdrawDao withdrawDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CapitalDao capitalDao;

    /**
     * 记录用户提现记录
     * @param withdraw
     * @return
     */
    public boolean recordWithdraw(Withdraw withdraw) throws Exception {
        if (withdraw.getWithdraw_money() <= 0) {
            throw new BusinessException("提现金额错误", 888888);
        }
        if (this.todayWithdraw(withdraw.getWithdraw_user_id())) {
            throw new BusinessException("今天已经进行过提现，请明天再来提现吧", 888888);
        }

		// 生成订单ID
		String orderId = heepayHelper.createOrderNo(0, withdraw.getWithdraw_user_id());

        // 在创建提现订单的时候，就生成提现订单ID
		withdraw.setWithdraw_batch_no(orderId);
        withdraw.setWithdraw_status(EWithdrawStatus.INIT.getCode());
        withdraw.setWithdraw_create_time(new Date());
        this.save(withdraw);

        // 冻结用户提现金额
        User user = this.userDao.findById(withdraw.getWithdraw_user_id());
        if (user.getUser_available_money() < withdraw.getWithdraw_money()) {
            throw new BusinessException("余额不足", 888888);
        }
        user.setUser_available_money(user.getUser_available_money() - withdraw.getWithdraw_money());
        user.setUser_freeze_money(user.getUser_freeze_money() + withdraw.getWithdraw_money());
        this.userDao.update(user);
        return true;
    }

    /**
     * 处理提现，进行提现到银行卡的操作
     * @param withdraw
     * @return
     * @throws Exception
     */
    public Boolean doWithdraw(Withdraw withdraw, Integer status) throws Exception {
        withdraw = this.withdrawDao.getById(withdraw.getWithdraw_id());

        if (withdraw.getWithdraw_status() != EWithdrawStatus.INIT.getCode()) {
            return false;
        }
        if (Objects.equals(status, EWithdrawStatus.INHAND.getCode())) {
            // 审核通过，处理中
            String batchNo = withdraw.getWithdraw_batch_no();
            Bank bank = this.bankDao.findById(withdraw.getWithdraw_bank_id());
            String detailData = String.format("%s^%s^%s^%s^%s^%s^%s^%s^%s^%s",
                    withdraw.getWithdraw_id(), bank.getBank_code(), 0, bank.getBank_user_code(), bank.getBank_user_name().trim(), withdraw.getWithdraw_money(), "投资笔记提现",
                    bank.getBank_add_province().trim(), bank.getBank_add_city().trim(), bank.getBank_open_bank().trim());

            BatchPayEntity batchPayEntity = new BatchPayEntity();
            batchPayEntity.setVersion("2");
            batchPayEntity.setAgent_id(this.heepayHelper.getHeepayAgentId());
            batchPayEntity.setBatch_amt(String.valueOf(withdraw.getWithdraw_money()));
            batchPayEntity.setBatch_no(batchNo);
            batchPayEntity.setBatch_num("1");
            batchPayEntity.setDetail_data(detailData);
            batchPayEntity.setNotify_url(this.heepayHelper.getHeepayBatchNotifyUrl());
            batchPayEntity.setExt_param1("withdraw");

            // 变更数据表提现状态
            withdraw.setWithdraw_status(EWithdrawStatus.INHAND.getCode());
            this.withdrawDao.update(withdraw);

            return this.heepayHelper.batchPay(batchPayEntity);
        }
        if (Objects.equals(status, EWithdrawStatus.NO.getCode())) {
            // 不予处理
            User user = this.userDao.findById(withdraw.getWithdraw_user_id());
            user.setUser_available_money(user.getUser_available_money() + withdraw.getWithdraw_money());
            user.setUser_freeze_money(user.getUser_freeze_money() - withdraw.getWithdraw_money());
            this.userDao.update(user);
            withdraw.setWithdraw_status(EWithdrawStatus.NO.getCode());
            this.withdrawDao.update(withdraw);
        }
        return true;
    }

    /**
     * 检查汇付宝下发批付结果
     * @param wId
     * @return
     * @throws Exception
     */
    public boolean checkWithdraw(Integer wId) throws Exception {
        Withdraw withdraw = this.withdrawDao.getById(wId);
        List<Map<String, Object>> heepayResultList = this.heepayHelper.checkHeepayBatchResult(String.valueOf(withdraw.getWithdraw_batch_no()));
        if (heepayResultList.size() == 0) {
            throw new BusinessException("汇付宝批付查询返回结果条数为0", 888888);
        }
        LOGGER.info("汇付宝批付查询返回的接口数据：{}", heepayResultList);
        for (Map<String, Object> aHeepayResultList : heepayResultList) {
            Withdraw order = this.withdrawDao.getById(Integer.valueOf(aHeepayResultList.get("orderNo").toString()));
            if (order == null) {
                throw new BusinessException("提现订单未找到 " + aHeepayResultList.get("orderNo").toString(), 888888);
            }
            if (!"S".equals(aHeepayResultList.get("status"))) {
                if ("F".equals(aHeepayResultList.get("status"))) {
                    // 处理失败了
                    order.setWithdraw_status(EWithdrawStatus.FAIL.getCode());
                    this.withdrawDao.update(order);
                    User user = this.userDao.getById(order.getWithdraw_user_id());
                    user.setUser_available_money(user.getUser_available_money() + order.getWithdraw_money());
                    user.setUser_freeze_money(user.getUser_freeze_money() - order.getWithdraw_money());
                    this.userDao.update(user);
                    return true;
                } else {
                    // 返回值不等于 S，没有提现成功呢，记录日志，不进行操作
                    LOGGER.warn("订单 {} 支付结果未成功，继续等待", aHeepayResultList.get("orderNo"));
                    //throw new BusinessException("订单支付未到账，请继续等待", 888888);
                    return false;
                }
            }
            // 提现成功到账了，进行成功的操作
            User user = this.userDao.findById(order.getWithdraw_user_id());
            // 将用户冻结的资金减少
            user.setUser_freeze_money(user.getUser_freeze_money() - order.getWithdraw_money());
            this.userDao.update(user);
            // 记录用户的资金流水
            Capital capital = new Capital();
            capital.setCapital_user_id(order.getWithdraw_user_id());
            capital.setCapital_type(ECapitalType.WITHDRAW.getCode());
            capital.setCapital_mount(-order.getWithdraw_money());
            capital.setCapital_available(user.getUser_available_money());
            capital.setCapital_freeze(user.getUser_freeze_money());
            capital.setCapital_create_time(new Date());
            this.capitalDao.save(capital);
            // 变更用户提现表状态
            withdraw.setWithdraw_status(EWithdrawStatus.OK.getCode());
            this.withdrawDao.update(withdraw);
            return true;
        }
        return false;
    }

    /**
     * 批付结果返回处理
     * @param batchResEntity
     * @return
     * @throws Exception
     */
    public boolean resWithdraw(BatchResEntity batchResEntity) throws Exception {
        // 验证签名
        String _str = String.format("ret_code=%s&ret_msg=%s&agent_id=%s&hy_bill_no=%s&status=%s&batch_no=%s&batch_amt=%s&batch_num=%s&detail_data=%s&ext_param1=%s&key=%s",
                batchResEntity.getRet_code(), batchResEntity.getRet_msg(), this.heepayHelper.getHeepayAgentId(), batchResEntity.getHy_bill_no(),
                batchResEntity.getStatus(), batchResEntity.getBatch_no(), batchResEntity.getBatch_amt(), batchResEntity.getBatch_num(),
                batchResEntity.getDetail_data(), batchResEntity.getExt_param1(), this.heepayHelper.getHeepayTixianKey());
        LOGGER.info("提现批付返回签名字符串："+_str);
        String sign = Md5Tools.MD5(_str.toLowerCase()).toLowerCase();
        LOGGER.info("提现批付返回签名："+sign);

		// TODO 校验返回的 签名 与  之前的签名是否相等（编码问题）

		if (batchResEntity.getRet_code().equals("0000")) {
			List<Map<String, Object>> dataList = this.analysisHeepayBatchDetailData(batchResEntity.getDetail_data());
			for (Map<String, Object> aDataList : dataList) {
				if (aDataList.get("status").toString().equals("S")) {
					// 查询提现订单
					Withdraw withdraw = this.withdrawDao.findById(Integer.valueOf(aDataList.get("orderNo").toString()));
					if (withdraw.getWithdraw_status() == EWithdrawStatus.INHAND.getCode()) {
						User user = this.userDao.findById(withdraw.getWithdraw_user_id());
						// 将用户冻结的资金减少
						user.setUser_freeze_money(user.getUser_freeze_money() - withdraw.getWithdraw_money());
						this.userDao.update(user);
						// 记录用户的资金流水
						Capital capital = new Capital();
						capital.setCapital_user_id(withdraw.getWithdraw_user_id());
						capital.setCapital_type(ECapitalType.WITHDRAW.getCode());
						capital.setCapital_mount(-withdraw.getWithdraw_money());
						capital.setCapital_available(user.getUser_available_money());
						capital.setCapital_freeze(user.getUser_freeze_money());
						capital.setCapital_create_time(new Date());
						this.capitalDao.save(capital);
						// 变更用户提现表状态
						withdraw.setWithdraw_status(EWithdrawStatus.OK.getCode());
						this.withdrawDao.update(withdraw);
					}
				}
			}
			return true;
		} else {
			// 处理失败
			return false;
		}
    }

    /**
     * 今天是否有提现，有的话返回true，没有返回false
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public boolean todayWithdraw(Integer userId) throws Exception {
		String today = DateUtil.convertDateToString("yyyy-MM-dd", new Date());
        String where = "withdraw_user_id = '"+userId+"' AND withdraw_status IN(1,2,3) AND withdraw_create_time >= '"+today+" 00:00:00' AND withdraw_create_time <= '"+today+" 23:59:59'";
        Integer count = this.withdrawDao.findAllCount(new Withdraw(), where);
        return count > 0;
    }

    /**
     * 解析汇付宝批付回调参数中的detail_data字段
     *
     * @param detailStr 字符串
     * @return 可读性的列表
     */
    private List<Map<String, Object>> analysisHeepayBatchDetailData(String detailStr) {
        List<Map<String, Object>> detailList = new ArrayList<>();
        String[] detailArr = detailStr.split("\\|");
        for (String aDetailArr : detailArr) {
            Map<String, Object> map = new HashMap<String, Object>();
            String[] subDetailArr = aDetailArr.split("\\^");
            map.put("orderNo", subDetailArr[0]);
            map.put("bankCardNo", subDetailArr[1]);
            map.put("bankCardName", subDetailArr[2]);
            map.put("amount", subDetailArr[3]);
            map.put("status", subDetailArr[4]);
            detailList.add(map);
        }
        return detailList;
    }

}

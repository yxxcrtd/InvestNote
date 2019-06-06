package com.caimao.weixin.note.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.weixin.note.dao.BankDao;
import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.domain.Bank;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.enums.EBankStatus;
import com.caimao.weixin.note.service.BankService;
import com.caimao.weixin.note.util.execption.BusinessException;

@Service
public class BankServiceImpl extends BaseServiceImpl<Bank, Integer> implements BankService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BankDao bankDao;

    /**
     * 银行代码
     */
	@SuppressWarnings("serial")
	public Map<String, String> bankCodeMap = new HashMap<String, String>() {
        {
            put("1", "工商银行");
            put("2", "建设银行");
            put("3", "农业银行");
            put("4", "邮政储蓄银行");
            put("5", "中国银行");
            put("6", "交通银行");
            put("7", "招商银行");
            put("8", "光大银行");
            put("9", "浦发银行");
            put("10", "华夏银行");
            put("11", "广东发展银行");
            put("12", "中信银行");
            put("13", "兴业银行");
            put("14", "民生银行");
            put("15", "杭州银行");
            put("16", "上海银行");
            put("17", "宁波银行");
            put("18", "平安银行");
        }
    };

    /**
     * 绑定用户银行卡
     * @param bank
     * @return
     */
    public boolean bindBank(Bank bank) {
        // 检查是否有已经绑定的银行卡
        Bank refBank = this.bankDao.findByUserIdAndStatus(bank.getBank_user_id(), EBankStatus.BIND.getCode());
        if (refBank != null) {
            return false;
        }

        // 如果用户表中有用户手机号，直接使用用户表中的手机号，没有的话，则使用新绑定的手机号进行更新用户信息
        User user = this.userDao.findById(bank.getBank_user_id());
        if (user.getUser_phone() != null && !user.getUser_phone().equals("")) {
            bank.setBank_phone(user.getUser_phone());
        } else {
            // 查询用户中是否有此手机号
            User phoneUser = this.userDao.findByPhone(bank.getBank_phone());
            if (phoneUser != null) {
                throw new BusinessException("手机号已经存在，请更换", 888888);
            }
            user.setUser_phone(bank.getBank_phone());
            this.userDao.update(user);
        }
        bank.setBank_name(this.bankCodeMap.get(bank.getBank_code()));
        bank.setBank_status(EBankStatus.BIND.getCode());
        bank.setBank_create_time(new Date());
        this.save(bank);
        return false;
    }

    /**
     * 解绑用户银行卡
     * @param userId
     * @param bankId
     * @return
     */
    public boolean unBindBank(Integer userId, Integer bankId) {
        Bank bank = this.bankDao.getById(bankId);
        if (bank != null && bank.getBank_user_id() == userId) {
            bank.setBank_status(EBankStatus.UNBIND.getCode());
            this.bankDao.update(bank);
        }
        return false;
    }
}

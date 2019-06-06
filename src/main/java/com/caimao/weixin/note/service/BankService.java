package com.caimao.weixin.note.service;

import com.caimao.weixin.note.domain.Bank;

public interface BankService extends BaseService<Bank, Integer> {

    public boolean bindBank(Bank bank);

    public boolean unBindBank(Integer userId, Integer bankId);

}

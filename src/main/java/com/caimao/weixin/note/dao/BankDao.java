package com.caimao.weixin.note.dao;

import com.caimao.weixin.note.domain.Bank;

public interface BankDao extends BaseDao<Bank, Integer> {

    public Bank findByUserIdAndStatus(Integer userId, Integer status);

}

package com.caimao.weixin.note.dao.impl;

import com.caimao.weixin.note.dao.BankDao;
import com.caimao.weixin.note.domain.Bank;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BankDaoImpl extends BaseDaoImpl<Bank, Integer, String, String> implements BankDao {

    public Bank findByUserIdAndStatus(Integer userId, Integer status) {
        String sql = "SELECT * FROM wx_bank WHERE bank_user_id = ? AND bank_status = ?";
        LOGGER.info(sql);

        List<Bank> bankList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Bank.class), new Object[]{userId, status});
        return bankList != null && bankList.size() > 0 ? bankList.get(0) : null;
    }
}

package com.caimao.weixin.note.dao.impl;

import com.caimao.weixin.note.dao.AdminUserDao;
import com.caimao.weixin.note.domain.AdminUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminUserDaoImpl extends BaseDaoImpl<AdminUser, Integer, String, String> implements AdminUserDao {

    @Override
    public AdminUser findByAccountAndPassword(String account, String password) {
        String sql = "SELECT * FROM wx_adminuser WHERE adminuser_account = ? AND adminuser_pwd = ?";
        List<AdminUser> adminUserList = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AdminUser.class), new Object[]{account, password});
        return adminUserList != null && adminUserList.size() > 0 ? adminUserList.get(0) : null;
    }
}

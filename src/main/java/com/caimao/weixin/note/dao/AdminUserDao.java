package com.caimao.weixin.note.dao;

import com.caimao.weixin.note.domain.AdminUser;

public interface AdminUserDao extends BaseDao<AdminUser, Integer> {

    public AdminUser findByAccountAndPassword(String account, String password);
}

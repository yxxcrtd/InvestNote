package com.caimao.weixin.note.service;

import com.caimao.weixin.note.domain.AdminUser;

public interface AdminUserService extends BaseService<AdminUser, Integer> {

    public Boolean login(String account, String password);

    public String createPwd(String pwd);

}

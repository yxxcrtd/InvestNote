package com.caimao.weixin.note.service.impl;

import com.caimao.weixin.note.dao.AdminUserDao;
import com.caimao.weixin.note.domain.AdminUser;
import com.caimao.weixin.note.service.AdminUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser, Integer> implements AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;

    @Override
    public Boolean login(String account, String password) {
        AdminUser checkUser = this.adminUserDao.findByAccountAndPassword(account, this.createPwd(password));
        if (checkUser == null) return false;
        AdminUser adminUser = this.adminUserDao.findById(checkUser.getAdminuser_id());
        adminUser.setAdminuser_last_login_time(new Date());
        this.adminUserDao.update(adminUser);
        return true;
    }

    /**
     * 加密后台密码
     * @param pwd
     * @return
     */
    public String createPwd(String pwd) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(pwd) + "hello note admin");
    }
}
package com.caimao.weixin.note.dao;

import java.util.List;
import java.util.Map;

import com.caimao.weixin.note.domain.User;

public interface UserDao extends BaseDao<User, Integer> {

    User findByOpenId(String openId);

    User findByPhone(String phone);
    
    public List<Map<String, Object>> findAdminTotalAmountDay();

}

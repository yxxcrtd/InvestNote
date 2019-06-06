package com.caimao.weixin.note.service;

import com.caimao.weixin.note.domain.User;

import java.util.Map;

public interface UserService extends BaseService<User, Integer> {

	User findByOpenId(String openId);

	User upsertUser(Map<String, String> userMap) throws Exception;

}

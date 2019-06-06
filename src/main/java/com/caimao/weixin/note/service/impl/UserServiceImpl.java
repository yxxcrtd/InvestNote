package com.caimao.weixin.note.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caimao.weixin.note.dao.StatDao;
import com.caimao.weixin.note.dao.UserDao;
import com.caimao.weixin.note.domain.Stat;
import com.caimao.weixin.note.domain.User;
import com.caimao.weixin.note.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService {

    @Autowired
    private StatDao statDao;

    public User findByOpenId(String openId) {
        return ((UserDao) baseDao).findByOpenId(openId);
    }

    /**
     * 1，保存新用户
     * 2，修改用户的NickName，HeadImgUrl
     * 3，增加统计表
     */
    public User upsertUser(Map<String, String> userMap) throws Exception {
        String openId = userMap.get("openid");
        if (null == openId) {
            return null;
        }

        // 获取用户对象
        User user = ((UserDao) baseDao).findByOpenId(openId);
        if (null == user) {
            // 保存新用户
            saveUser(userMap);

            // 更新统计表中的用户信息
            Stat stat = statDao.findById(1);
            stat.setStat_user(stat.getStat_user() + 1);
            statDao.update(stat);
        } else {
			updateUser(user);
        }

        // 返回用户对象
        return user;
    }

    // 保存新用户
    private User saveUser(Map<String, String> userMap) {
        User u = new User();
        u.setUser_open_id(userMap.get("openid"));
        u.setUser_nickname(userMap.get("nickname"));
        u.setUser_header_img(userMap.get("headimgurl"));
        u.setUser_subscribe(0);
        u.setUser_note_count(0);
        u.setUser_success(0);
        u.setUser_yield(0);
        u.setUser_phone("");
        u.setUser_bank_code("");
        u.setUser_available_money(0);
        u.setUser_freeze_money(0);
        u.setUser_update_time(new Date());
        u.setUser_create_time(new Date());
        baseDao.save(u);
        return u;
    }

    // 更新用户基本信息（昵称、头像），24小时更新一次
    private void updateUser(User user) {
        if (new Date().getTime() - user.getUser_update_time().getTime() >= (24 * 60 * 60 * 1000)) {
            //
//            Map<String, String> wxMap = this.getUserInfoByOpenId(user.getUser_open_id());
//            if (null == wxMap) {
//                return;
//            }
//            User u = new User();
//            u.setUser_id(user.getUser_id());
//            u.setUser_open_id(user.getUser_open_id());
//            u.setUser_nickname(wxMap.get("nickname"));
//            u.setUser_header_img(wxMap.get("headimgurl"));
//            u.setUser_subscribe(null == Integer.getInteger(wxMap.get("subscribe")) ? 0 : Integer.getInteger(wxMap.get("subscribe")));
//            u.setUser_note_count(user.getUser_note_count());
//            u.setUser_success(user.getUser_success());
//            u.setUser_yield(user.getUser_yield());
//            u.setUser_phone(user.getUser_phone());
//            u.setUser_bank_code(user.getUser_bank_code());
//            u.setUser_available_money(user.getUser_available_money());
//            u.setUser_freeze_money(user.getUser_freeze_money());
//            u.setUser_create_time(user.getUser_create_time());
//            u.setUser_update_time(new Date());
//            baseDao.update(u);
        }
    }

}


/**
 * 获取用户信息
 * @param openId
 * @return
 */
//    protected Map<String, String> getUserInfoByOpenId(String openId) throws Exception {
//        // redis进行缓存
//        Object httpRes = this.redisUtil.get(openId);
//        if (httpRes == null) {
//            String url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", this.getAccessToken(), openId);
//            httpRes = HttpHelper.doGet(url);
//            try {
//                // 验证返回值是否正确，错误的话会抛出异常，正确的话，加到redis cache中
//                WXMessageUtil.verifiedResults(httpRes.toString());
//                this.redisUtil.set(openId, JSON.toJSONString(httpRes.toString(), SerializerFeature.BrowserCompatible), redisCacheUserInfoExpires);
//            } catch (Exception ignored) {}
//        } else {
//            httpRes = JSON.parseObject(httpRes.toString(), String.class);
//        }
//        LOGGER.info("根据openId获取userInfo返回值：{}", httpRes.toString());
//        return WXMessageUtil.parseJson(httpRes.toString());
//    }
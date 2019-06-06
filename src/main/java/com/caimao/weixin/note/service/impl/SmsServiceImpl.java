package com.caimao.weixin.note.service.impl;

import com.caimao.weixin.note.dao.SmsDao;
import com.caimao.weixin.note.domain.Sms;
import com.caimao.weixin.note.service.SmsService;
import com.caimao.weixin.note.util.RedisUtil;
import com.caimao.weixin.note.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsServiceImpl extends BaseServiceImpl<Sms, Integer> implements SmsService {

    @Autowired
    private SmsDao smsDao;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private RedisUtil redisUtil;

    public Boolean sendSmsCode(Integer userId, String mobile) {
        if (mobile == null) {
            return false;
        }
        Sms sms = new Sms();
        sms.setSms_user_id(userId);
        sms.setSms_phone(mobile);
        sms.setSms_status(0);
        sms.setSms_create_time(new Date());

        String smsCode = this.generatorSmsCode();
        String smsContent = String.format("投资笔记短信验证码：%s，请在五分钟内使用。", smsCode);
        sms.setSms_content(smsContent);
        try {
            Boolean sendRes = this.smsUtil.sendSms(mobile, smsContent);
            if (sendRes) sms.setSms_status(1);
        } catch (Exception ignored) {
        }
        this.smsDao.save(sms);

        // 短信验证码记录到redis中
        String redisKey = userId.toString() + mobile;
        this.redisUtil.set(redisKey, smsCode, 5 * 60 * 1000);
        return true;
    }

    public Boolean verifySmsCode(Integer userId, String mobile, String smsCode) {
//        return true;
        String redisKey = userId.toString() + mobile;
        Object redisVal = this.redisUtil.get(redisKey);
        if (redisVal == null) {
            return false;
        }
        if (redisVal.toString().equals(smsCode)) {
            // 验证完成后清空
            this.redisUtil.del(redisKey);
            return true;
        }
        return false;
    }

    private String generatorSmsCode() {
        String smsCode = "";
        for (int i = 1; i <= 6; i++) {
            smsCode += String.valueOf((int) (Math.random() * 10));
        }
        return smsCode;
    }
}

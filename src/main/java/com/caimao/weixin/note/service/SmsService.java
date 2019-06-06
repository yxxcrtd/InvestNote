package com.caimao.weixin.note.service;

import com.caimao.weixin.note.domain.Sms;

public interface SmsService extends BaseService<Sms, Integer> {

    public Boolean sendSmsCode(Integer userId, String mobile);

    public Boolean verifySmsCode(Integer userId, String mobile, String smsCode);

}

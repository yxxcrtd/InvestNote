package com.caimao.weixin.note.dao.impl;

import com.caimao.weixin.note.dao.SmsDao;
import com.caimao.weixin.note.domain.Sms;
import org.springframework.stereotype.Service;

@Service
public class SmsDaoImpl extends BaseDaoImpl<Sms, Integer, String, String> implements SmsDao {

}

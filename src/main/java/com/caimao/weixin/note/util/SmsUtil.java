package com.caimao.weixin.note.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caimao.weixin.note.util.execption.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 发短信
 */
@Service
public class SmsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsUtil.class);

    @Value("${SMSsdkAppid}")
    private String sdkAppId;
    @Value("${SMSsdkAppkey}")
    private String sdkAppKey;


    public Boolean sendSms(String phone, String content) throws Exception {
        String url = "https://yun.tim.qq.com/v3/tlssmssvr/sendsms?sdkappid="+this.sdkAppId;
        Map<String, String> tel = new TreeMap<>();
        tel.put("nationcode", "86");
        tel.put("phone", phone);
        Map<String, Object> req = new TreeMap<>();
        req.put("tel", tel);
        req.put("type", "0");
        req.put("msg", content);
        req.put("sig", this.MD5(this.sdkAppKey+phone));
        req.put("extend", "");
        req.put("ext", "TZBJ");

        String res = HttpHelper.doPost(url, JSON.toJSON(req));
        if (res == null) {
            throw new BusinessException("发送短信失败", 888888);
        }
        JSONObject jsonObj = JSONArray.parseObject(res);
        if (Objects.equals(jsonObj.get("result").toString(), "0")) {
            return true;
        }
        LOGGER.error("短信发送错误: URL : {} : {}", url, jsonObj);
        return false;
    }

    public String MD5(String s) throws Exception {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        //与汇付宝编码一致
        byte[] btInput = s.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }
}

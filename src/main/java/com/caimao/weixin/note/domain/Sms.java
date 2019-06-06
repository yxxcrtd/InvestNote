package com.caimao.weixin.note.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class Sms implements Serializable {

    private Integer sms_id;

    private Integer sms_user_id;

    private String sms_phone;

    private String sms_content;

    private Integer sms_status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sms_create_time;

    public Integer getSms_id() {
        return sms_id;
    }

    public void setSms_id(Integer sms_id) {
        this.sms_id = sms_id;
    }

    public Integer getSms_user_id() {
        return sms_user_id;
    }

    public void setSms_user_id(Integer sms_user_id) {
        this.sms_user_id = sms_user_id;
    }

    public String getSms_phone() {
        return sms_phone;
    }

    public void setSms_phone(String sms_phone) {
        this.sms_phone = sms_phone;
    }

    public String getSms_content() {
        return sms_content;
    }

    public void setSms_content(String sms_content) {
        this.sms_content = sms_content;
    }

    public Integer getSms_status() {
        return sms_status;
    }

    public void setSms_status(Integer sms_status) {
        this.sms_status = sms_status;
    }

    public Date getSms_create_time() {
        return sms_create_time;
    }

    public void setSms_create_time(Date sms_create_time) {
        this.sms_create_time = sms_create_time;
    }

}

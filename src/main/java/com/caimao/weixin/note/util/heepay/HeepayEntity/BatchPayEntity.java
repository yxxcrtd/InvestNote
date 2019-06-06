package com.caimao.weixin.note.util.heepay.HeepayEntity;

/**
 * 批付的请求对象
 */
public class BatchPayEntity {
    private String version;
    private String agent_id;
    private String batch_no;
    private String batch_amt;
    private String batch_num;
    private String detail_data;
    private String notify_url;
    private String ext_param1;
    private String sign;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getBatch_amt() {
        return batch_amt;
    }

    public void setBatch_amt(String batch_amt) {
        this.batch_amt = batch_amt;
    }

    public String getBatch_num() {
        return batch_num;
    }

    public void setBatch_num(String batch_num) {
        this.batch_num = batch_num;
    }

    public String getDetail_data() {
        return detail_data;
    }

    public void setDetail_data(String detail_data) {
        this.detail_data = detail_data;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getExt_param1() {
        return ext_param1;
    }

    public void setExt_param1(String ext_param1) {
        this.ext_param1 = ext_param1;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

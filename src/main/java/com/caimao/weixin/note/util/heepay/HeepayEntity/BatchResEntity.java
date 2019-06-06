package com.caimao.weixin.note.util.heepay.HeepayEntity;

/**
 * 批付结果通知返回对象
 */
public class BatchResEntity {
    private String ret_code;
    private String ret_msg;
    private String agent_id;
    private String hy_bill_no;
    private String status;
    private String batch_no;
    private String batch_amt;
    private String batch_num;
    private String detail_data;
    private String ext_param1;
    private String sign;

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getHy_bill_no() {
        return hy_bill_no;
    }

    public void setHy_bill_no(String hy_bill_no) {
        this.hy_bill_no = hy_bill_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

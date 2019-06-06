package com.caimao.weixin.note.domain;


import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class Withdraw extends BaseDomain implements Serializable {

	private int withdraw_id;

	private String withdraw_batch_no;

	private int withdraw_user_id;

	@NotNull
	@Min(value = 1, message = "请选择银行")
	private int withdraw_bank_id;

	@NotNull
	@DecimalMin(value = "1.00", message = "最小提现金额为1元")
	@DecimalMax(value = "20000.00", message = "最大提现金额为2万元")
	@Digits(integer = 5, fraction = 2)
	private double withdraw_money;

	private int withdraw_status;

	/** 时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date withdraw_create_time;

	public int getWithdraw_id() {
		return withdraw_id;
	}

	public void setWithdraw_id(int withdraw_id) {
		this.withdraw_id = withdraw_id;
	}

	public String getWithdraw_batch_no() {
		return withdraw_batch_no;
	}

	public void setWithdraw_batch_no(String withdraw_batch_no) {
		this.withdraw_batch_no = withdraw_batch_no;
	}

	public int getWithdraw_user_id() {
		return withdraw_user_id;
	}

	public void setWithdraw_user_id(int withdraw_user_id) {
		this.withdraw_user_id = withdraw_user_id;
	}

	public int getWithdraw_bank_id() {
		return withdraw_bank_id;
	}

	public void setWithdraw_bank_id(int withdraw_bank_id) {
		this.withdraw_bank_id = withdraw_bank_id;
	}

	public double getWithdraw_money() {
		return withdraw_money;
	}

	public void setWithdraw_money(double withdraw_money) {
		this.withdraw_money = withdraw_money;
	}

	public int getWithdraw_status() {
		return withdraw_status;
	}

	public void setWithdraw_status(int withdraw_status) {
		this.withdraw_status = withdraw_status;
	}

	public Date getWithdraw_create_time() {
		return withdraw_create_time;
	}

	public void setWithdraw_create_time(Date withdraw_create_time) {
		this.withdraw_create_time = withdraw_create_time;
	}
}

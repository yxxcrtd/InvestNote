package com.caimao.weixin.note.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Bank implements Serializable {

	private int bank_id;

	private int bank_user_id;

	@NotNull
	@NotEmpty
	private String bank_code;

	private String bank_name;

	@NotNull
	@Length(min = 1, max = 10, message = "请输入账户姓名")
	private String bank_user_name;

	@NotNull
	@Length(min = 8, max = 20, message = "请输入正确的卡号")
	private String bank_user_code;

	@NotEmpty
	private String bank_add_province;

	@NotEmpty
	private String bank_add_city;

	@NotEmpty
	private String bank_open_bank;

	private int bank_status;

	/** 创建时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date bank_create_time;

	@NotNull
	@Length(min = 11, max = 11, message = "手机号不正确")
	private String bank_phone;

	public int getBank_id() {
		return bank_id;
	}

	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}

	public int getBank_user_id() {
		return bank_user_id;
	}

	public void setBank_user_id(int bank_user_id) {
		this.bank_user_id = bank_user_id;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_user_name() {
		return bank_user_name;
	}

	public void setBank_user_name(String bank_user_name) {
		this.bank_user_name = bank_user_name;
	}

	public String getBank_user_code() {
		return bank_user_code;
	}

	public void setBank_user_code(String bank_user_code) {
		this.bank_user_code = bank_user_code;
	}

	public String getBank_add_province() {
		return bank_add_province;
	}

	public void setBank_add_province(String bank_add_province) {
		this.bank_add_province = bank_add_province;
	}

	public String getBank_add_city() {
		return bank_add_city;
	}

	public void setBank_add_city(String bank_add_city) {
		this.bank_add_city = bank_add_city;
	}

	public String getBank_open_bank() {
		return bank_open_bank;
	}

	public void setBank_open_bank(String bank_open_bank) {
		this.bank_open_bank = bank_open_bank;
	}

	public int getBank_status() {
		return bank_status;
	}

	public void setBank_status(int bank_status) {
		this.bank_status = bank_status;
	}

	public Date getBank_create_time() {
		return bank_create_time;
	}

	public void setBank_create_time(Date bank_create_time) {
		this.bank_create_time = bank_create_time;
	}

	public String getBank_phone() {
		return bank_phone;
	}

	public void setBank_phone(String bank_phone) {
		this.bank_phone = bank_phone;
	}
}

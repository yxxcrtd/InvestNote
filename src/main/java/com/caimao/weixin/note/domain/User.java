package com.caimao.weixin.note.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class User extends BaseDomain {

	private int user_id;

	private String user_open_id;

	private String user_nickname;

	private String user_header_img;

	/** 用户是否关注公众号：0未关注；1已关注 */
	private int user_subscribe;

	/** 当前用户创建的笔记总数 */
	private int user_note_count;

	/** 笔记成功率 */
	private float user_success;

	/** 平均收益（ 每次预期的平均数） */
	private float user_yield;

	private String user_phone;

	private String user_bank_code;

	/** 可用资金 */
	private double user_available_money;

	/** 冻结资金 */
	private double user_freeze_money;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date user_update_time = new Date();

	/** 时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date user_create_time = new Date();

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_open_id() {
		return user_open_id;
	}

	public void setUser_open_id(String user_open_id) {
		this.user_open_id = user_open_id;
	}

	public String getUser_nickname() {
		return user_nickname;
	}

	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}

	public String getUser_header_img() {
		return user_header_img;
	}

	public void setUser_header_img(String user_header_img) {
		this.user_header_img = user_header_img;
	}

	public int getUser_subscribe() {
		return user_subscribe;
	}

	public void setUser_subscribe(int user_subscribe) {
		this.user_subscribe = user_subscribe;
	}

	public int getUser_note_count() {
		return user_note_count;
	}

	public void setUser_note_count(int user_note_count) {
		this.user_note_count = user_note_count;
	}

	public float getUser_success() {
		return user_success;
	}

	public void setUser_success(float user_success) {
		this.user_success = user_success;
	}

	public float getUser_yield() {
		return user_yield;
	}

	public void setUser_yield(float user_yield) {
		this.user_yield = user_yield;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_bank_code() {
		return user_bank_code;
	}

	public void setUser_bank_code(String user_bank_code) {
		this.user_bank_code = user_bank_code;
	}

	public double getUser_available_money() {
		return user_available_money;
	}

	public void setUser_available_money(double user_available_money) {
		this.user_available_money = user_available_money;
	}

	public double getUser_freeze_money() {
		return user_freeze_money;
	}

	public void setUser_freeze_money(double user_freeze_money) {
		this.user_freeze_money = user_freeze_money;
	}

	public Date getUser_update_time() {
		return user_update_time;
	}

	public void setUser_update_time(Date user_update_time) {
		this.user_update_time = user_update_time;
	}

	public Date getUser_create_time() {
		return user_create_time;
	}

	public void setUser_create_time(Date user_create_time) {
		this.user_create_time = user_create_time;
	}
}

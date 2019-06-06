package com.caimao.weixin.note.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@SuppressWarnings("serial")
public class Reader extends BaseDomain {

	private int reader_id;

	private int reader_user_id;

	private int reader_note_id;

	/** 阅读金额 */
	private double reader_money;
	
	/** 笔记踩赞状态(0:未操作;1:赞；2:踩) */
	private int reader_step_paise_status;
	

	/** 时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reader_create_time;

	public int getReader_id() {
		return reader_id;
	}

	public void setReader_id(int reader_id) {
		this.reader_id = reader_id;
	}

	public int getReader_user_id() {
		return reader_user_id;
	}

	public void setReader_user_id(int reader_user_id) {
		this.reader_user_id = reader_user_id;
	}

	public int getReader_note_id() {
		return reader_note_id;
	}

	public void setReader_note_id(int reader_note_id) {
		this.reader_note_id = reader_note_id;
	}

	public double getReader_money() {
		return reader_money;
	}

	public void setReader_money(double reader_money) {
		this.reader_money = reader_money;
	}

	public Date getReader_create_time() {
		return reader_create_time;
	}

	public void setReader_create_time(Date reader_create_time) {
		this.reader_create_time = reader_create_time;
	}

	public int getReader_step_paise_status() {
		return reader_step_paise_status;
	}

	public void setReader_step_paise_status(int reader_step_paise_status) {
		this.reader_step_paise_status = reader_step_paise_status;
	}
}

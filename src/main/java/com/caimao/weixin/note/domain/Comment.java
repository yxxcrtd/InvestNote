package com.caimao.weixin.note.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class Comment extends BaseDomain {

	/**
	 * 评论ID
	 */
	public int comment_id;
	
	/**
	 * 用户ID
	 */
	public int comment_user_id;
	
	/**
	 * 用户昵称
	 */
	public String comment_user_nickname;
	
	/**
	 * 用户头像
	 */
	public String comment_user_header_img;
	
	/**
	 * 笔记ID
	 */
	public int comment_note_id;
	
	/**
	 * 股票代码
	 */
	public String comment_stock_code;
	
	/**
	 * 股票名
	 */
	public String comment_stock_name;
	
	/**
	 * 投资逻辑
	 */
	public String comment_note_remark;
	
	/**
	 * 评论内容
	 */
	public String comment_content;
	
	/**
	 * 添加时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date comment_create_time;

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getComment_user_id() {
		return comment_user_id;
	}

	public void setComment_user_id(int comment_user_id) {
		this.comment_user_id = comment_user_id;
	}

	public int getComment_note_id() {
		return comment_note_id;
	}

	public void setComment_note_id(int comment_note_id) {
		this.comment_note_id = comment_note_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public Date getComment_create_time() {
		return comment_create_time;
	}

	public void setComment_create_time(Date comment_create_time) {
		this.comment_create_time = comment_create_time;
	}

	public String getComment_user_nickname() {
		return comment_user_nickname;
	}

	public void setComment_user_nickname(String comment_user_nickname) {
		this.comment_user_nickname = comment_user_nickname;
	}

	public String getComment_stock_code() {
		return comment_stock_code;
	}

	public void setComment_stock_code(String comment_stock_code) {
		this.comment_stock_code = comment_stock_code;
	}

	public String getComment_stock_name() {
		return comment_stock_name;
	}

	public void setComment_stock_name(String comment_stock_name) {
		this.comment_stock_name = comment_stock_name;
	}

	public String getComment_note_remark() {
		return comment_note_remark;
	}

	public void setComment_note_remark(String comment_note_remark) {
		this.comment_note_remark = comment_note_remark;
	}

	public String getComment_user_header_img() {
		return comment_user_header_img;
	}

	public void setComment_user_header_img(String comment_user_header_img) {
		this.comment_user_header_img = comment_user_header_img;
	}

}

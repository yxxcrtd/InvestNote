package com.caimao.weixin.note.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class Focus implements Serializable {

	/** 主键ID */
	private int focus_id;

	/** 关注的人 */
	private int focus_user_id;

	/** 关注别人 */
	private int focus_other_id;

	/** 创建时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date focus_create_time = new Date();

	public int getFocus_id() {
		return focus_id;
	}

	public void setFocus_id(int focus_id) {
		this.focus_id = focus_id;
	}

	public int getFocus_user_id() {
		return focus_user_id;
	}

	public void setFocus_user_id(int focus_user_id) {
		this.focus_user_id = focus_user_id;
	}

	public int getFocus_other_id() {
		return focus_other_id;
	}

	public void setFocus_other_id(int focus_other_id) {
		this.focus_other_id = focus_other_id;
	}

	public Date getFocus_create_time() {
		return focus_create_time;
	}

	public void setFocus_create_time(Date focus_create_time) {
		this.focus_create_time = focus_create_time;
	}

}

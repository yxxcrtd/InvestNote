package com.caimao.weixin.note.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class AdminUser extends BaseDomain {

	private Integer adminuser_id;
	private String adminuser_account;
	private String adminuser_pwd;
	private Integer adminuser_status;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date adminuser_created;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date adminuser_updated;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date adminuser_last_login_time;

	public Integer getAdminuser_id() {
		return adminuser_id;
	}

	public void setAdminuser_id(Integer adminuser_id) {
		this.adminuser_id = adminuser_id;
	}

	public String getAdminuser_account() {
		return adminuser_account;
	}

	public void setAdminuser_account(String adminuser_account) {
		this.adminuser_account = adminuser_account;
	}

	public String getAdminuser_pwd() {
		return adminuser_pwd;
	}

	public void setAdminuser_pwd(String adminuser_pwd) {
		this.adminuser_pwd = adminuser_pwd;
	}

	public Integer getAdminuser_status() {
		return adminuser_status;
	}

	public void setAdminuser_status(Integer adminuser_status) {
		this.adminuser_status = adminuser_status;
	}

	public Date getAdminuser_created() {
		return adminuser_created;
	}

	public void setAdminuser_created(Date adminuser_created) {
		this.adminuser_created = adminuser_created;
	}

	public Date getAdminuser_updated() {
		return adminuser_updated;
	}

	public void setAdminuser_updated(Date adminuser_updated) {
		this.adminuser_updated = adminuser_updated;
	}

	public Date getAdminuser_last_login_time() {
		return adminuser_last_login_time;
	}

	public void setAdminuser_last_login_time(Date adminuser_last_login_time) {
		this.adminuser_last_login_time = adminuser_last_login_time;
	}
}

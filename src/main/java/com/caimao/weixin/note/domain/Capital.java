package com.caimao.weixin.note.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 流水
 */
@SuppressWarnings("serial")
public class Capital extends BaseDomain implements Serializable {

	private int capital_id;

	private int capital_user_id;

	/** 1. 充值 2. 提现 3. 红包费 4.红包费退回 5.发红包 6.阅读费 7.阅读费退回 8.阅读费收益 9.技术服务费；10保证金 */
	private int capital_type;

	/** 资金变动金额 */
	private double capital_mount;

	/** 资金变动后可用资金 */
	private double capital_available;

	/** 资金变动后冻结 */
	private double capital_freeze;

	/** 创建时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date capital_create_time;

	public int getCapital_id() {
		return capital_id;
	}

	public void setCapital_id(int capital_id) {
		this.capital_id = capital_id;
	}

	public int getCapital_user_id() {
		return capital_user_id;
	}

	public void setCapital_user_id(int capital_user_id) {
		this.capital_user_id = capital_user_id;
	}

	public int getCapital_type() {
		return capital_type;
	}

	public void setCapital_type(int capital_type) {
		this.capital_type = capital_type;
	}

	public double getCapital_mount() {
		return capital_mount;
	}

	public void setCapital_mount(double capital_mount) {
		this.capital_mount = capital_mount;
	}

	public double getCapital_available() {
		return capital_available;
	}

	public void setCapital_available(double capital_available) {
		this.capital_available = capital_available;
	}

	public double getCapital_freeze() {
		return capital_freeze;
	}

	public void setCapital_freeze(double capital_freeze) {
		this.capital_freeze = capital_freeze;
	}

	public Date getCapital_create_time() {
		return capital_create_time;
	}

	public void setCapital_create_time(Date capital_create_time) {
		this.capital_create_time = capital_create_time;
	}
}

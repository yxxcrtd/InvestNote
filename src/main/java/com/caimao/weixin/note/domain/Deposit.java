package com.caimao.weixin.note.domain;


import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class Deposit implements Serializable {

	/** 充值ID */
	private int deposit_id;

	/** 订单ID */
	private String deposit_bill_id;

	/** 用户ID */
	private int deposit_user_id;

	/** 笔记ID */
	private int deposit_note_id;

	/** 充值金额 */
	private double deposit_money;

	/** 充值状态：1未处理；2充值成功；3失败 */
	private int deposit_status;

	/** 充值动作：1发布笔记；2阅读笔记 */
	private int deposit_action;

	/** 时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date deposit_create_time;

	public int getDeposit_id() {
		return deposit_id;
	}

	public void setDeposit_id(int deposit_id) {
		this.deposit_id = deposit_id;
	}

	public String getDeposit_bill_id() {
		return deposit_bill_id;
	}

	public void setDeposit_bill_id(String deposit_bill_id) {
		this.deposit_bill_id = deposit_bill_id;
	}

	public int getDeposit_user_id() {
		return deposit_user_id;
	}

	public void setDeposit_user_id(int deposit_user_id) {
		this.deposit_user_id = deposit_user_id;
	}

	public int getDeposit_note_id() {
		return deposit_note_id;
	}

	public void setDeposit_note_id(int deposit_note_id) {
		this.deposit_note_id = deposit_note_id;
	}

	public double getDeposit_money() {
		return deposit_money;
	}

	public void setDeposit_money(double deposit_money) {
		this.deposit_money = deposit_money;
	}

	public int getDeposit_status() {
		return deposit_status;
	}

	public void setDeposit_status(int deposit_status) {
		this.deposit_status = deposit_status;
	}

	public int getDeposit_action() {
		return deposit_action;
	}

	public void setDeposit_action(int deposit_action) {
		this.deposit_action = deposit_action;
	}

	public Date getDeposit_create_time() {
		return deposit_create_time;
	}

	public void setDeposit_create_time(Date deposit_create_time) {
		this.deposit_create_time = deposit_create_time;
	}
}

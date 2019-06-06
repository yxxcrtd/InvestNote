package com.caimao.weixin.note.enums;

/**
 * 用户资金变动类型
 */
public enum ECapitalType {
	DEPOSIT(1, "充值"), WITHDRAW(2, "提现"), REDPACKET(3, "红包费"), RETURNREDPACKET(4, "红包费退回"), SENDREDPACKET(5,
			"发红包"), READ(6, "阅读费"), RETURNREAD(7, "阅读费退回"), READINCOME(8, "阅读费收益"), SERVICEFEE(9, "技术服务费");

	private final Integer code;
	private final String value;

	private ECapitalType(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getValue() {
		return this.value;
	}

}
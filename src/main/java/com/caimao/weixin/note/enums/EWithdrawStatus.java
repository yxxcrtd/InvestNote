package com.caimao.weixin.note.enums;

/**
 * 提现状态
 */
public enum EWithdrawStatus {
	INIT(1, "未处理"), INHAND(2, "处理中"), OK(3, "提现成功"), FAIL(4, "提现失败"), NO(5, "不予处理");

	private final Integer code;
	private final String value;

	private EWithdrawStatus(Integer code, String value) {
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
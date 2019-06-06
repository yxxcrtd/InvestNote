package com.caimao.weixin.note.enums;

/**
 * 充值状态
 */
public enum EDepositStatus {
	INIT(1, "未处理"), OK(2, "成功"), FAIL(3, "失败");

	private final Integer code;
	private final String value;

	private EDepositStatus(Integer code, String value) {
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
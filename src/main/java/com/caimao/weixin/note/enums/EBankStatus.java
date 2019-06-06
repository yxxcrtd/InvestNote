package com.caimao.weixin.note.enums;

/**
 * 用户绑定银行卡状态
 */
public enum EBankStatus {
	BIND(1, "绑定正常"), UNBIND(2, "解绑状态");

	private final Integer code;
	private final String value;

	private EBankStatus(Integer code, String value) {
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
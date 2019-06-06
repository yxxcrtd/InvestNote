package com.caimao.weixin.note.enums;

public enum ENotePayStatus {
	INIT(1, "未支付"), OK(2, "已支付");

	private final Integer code;
	private final String value;

	private ENotePayStatus(Integer code, String value) {
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
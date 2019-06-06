package com.caimao.weixin.note.enums;

/**
 * 充值类型
 */
public enum EDepositType {
	NOTE(1, "创建笔记"), READ(2, "阅读笔记");

	private final Integer code;
	private final String value;

	private EDepositType(Integer code, String value) {
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
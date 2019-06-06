package com.caimao.weixin.note.util;

public enum Separator {
	SEMICOLON(";"), COMMA(","), COLON(":"), PERIOD(".");

	private String separator;

	private Separator(String separator) {
		this.separator = separator;
	}

	public String value() {
		return this.separator;
	}
}

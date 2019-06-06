package com.caimao.weixin.note.util;

import java.text.ParseException;

public class StringUtil {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

	}

	/**
	 * 过滤微信中的特殊符号
	 *
	 * @param s
	 * @return
	 */
	public static String filterWeiXinEmojiChar(String s) {
		if (null == s || "".equals(s)) {
			return "";
		}
		return s.replaceAll("[^\\u0000-\\uFFFF]", "").trim();
	}

}

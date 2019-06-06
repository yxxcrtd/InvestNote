package com.caimao.weixin.note.util;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串工具类
 */
public class StringHelper {

    /**
     * 隐藏字符中的某些字符
     * @param source    字符串
     * @param preDigit  前边保留
     * @param lastDigit 后边保留
     * @return 处理后的字符串
     */
    public static String hide(String source, int preDigit, int lastDigit) {
        int sum = preDigit + lastDigit;
        if (StringUtils.isNotBlank(source) && source.length() >= sum) {
            int length = source.length();
            String pre = source.substring(0, preDigit);
            String last = source.substring(length - lastDigit, length);
            StringBuffer sb = new StringBuffer(pre);
            for (int i = 0; i < length - sum; ++i) {
                sb.append("*");
            }
            sb.append(last);
            return sb.toString();
        } else {
            return null;
        }
    }

    public static String random(Integer len) {
        String str = "";
        for (int i = 1; i <= len; i++) {
            str += String.valueOf((int)(Math.random()*10));
        }
        return str;
    }

}

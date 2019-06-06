package com.caimao.weixin.note.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	/** LOGGER */
	protected static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	public static final String START_TIME_09_20_00 = "09:20:00";
	public static final String START_TIME_09_21_00 = "09:21:00";
	public static final String START_TIME_00_00_00 = "00:00:00";
	public static final String START_TIME_12_55_00 = "12:55:00";
	public static final String START_TIME_12_56_00 = "12:56:00";
	public static final String END_TIME_15_00_00 = "15:00:00";
	public static final String END_TIME_15_00_01 = "15:00:01";

	public static void main(String[] args) throws Exception {
		// String holiday = "2016-04-04,2016-05-02,2016-06-09,2016-09-15,2016-10-03,2016-10-04,2016-10-05,2016-10-06,2016-10-07";
		// System.out.println(getAfterWorkDay("15:00:00", 6, holiday));

		// System.out.println(isInHoliday("2016-10-06123123", holiday));

		// System.out.println(isInterval("2016-04-19 09:20:00", "2016-04-19 12:55:00", "2016-04-19 09:20:00", "yyyy-MM-dd HH:mm:ss"));

		// System.out.println(getDateTime("yyyy-MM-dd", new Date()));

		// 验证9点20之前的日志就不加1天
		// System.out.println(getAfterWorkDay("09:00:00", 1, holiday));

		// System.out.println(getNoteStartTime(new Date()));

		// System.out.println(getPreviousDay("yyyy-MM-dd", new Date()));
		// System.out.println(getPreviousDay("yyyy-MM-dd HH:mm:ss", new Date()));
	}

	public static boolean isInHoliday(String currentDate, String holiday) {
		boolean isHoliday = false;
		if (-1 < holiday.indexOf(currentDate.substring(0, 10))) {
			return true;
		}
		return isHoliday;
	}

	/**
	 * 从开始日期计算N天之后的日期（如果是当天9点20之前就不加一天）
	 */
	public static final Date getAfterWorkDay(String time, int days, String holiday) throws ParseException {
		Date date = new Date();
		String currentDate = convertDateToString("yyyy-MM-dd", date);
		String currentTime = convertDateToString("yyyy-MM-dd HH:mm:ss", date);
		Calendar now = Calendar.getInstance();
		now.setTime(convertStringToDate("yyyy-MM-dd HH:mm:ss", currentDate + " " + time));
		int i = 0;
		boolean isHoliday = false;
		while (i < days) {
			now.add(Calendar.DATE, 1);
			i++;
			// 如果是周六或周日，减1天
			if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				i--;
			}
			// 节假日
			for (String h : holiday.split(",")) {
				if (h.equals(convertDateToString("yyyy-MM-dd", now.getTime()))) {
					isHoliday = true;
					i--;
				}
			}
		}

		// 如果是当天12点55之前就不加一天
		if (isInterval(currentDate + " " + START_TIME_00_00_00, currentDate + " " + START_TIME_12_55_00, currentTime, "yyyy-MM-dd HH:mm:ss")) {
			now.add(Calendar.DATE, -1);
			if (isHoliday && 1 == days) {
				now.add(Calendar.DATE, -1);
			}
			if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				now.add(Calendar.DATE, -2);
			}
			if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				now.add(Calendar.DATE, -1);
			}
		}

		LOGGER.info("返回日期：" + now.getTime());

		return now.getTime();
	}
	
	public static String getPreviousDay(String mask, Date date) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -1);
		return convertDateToString(mask, now.getTime());
	}

	/**
	 * 日期转字符串，返回字符串的日期格式
	 * 
	 * @param mask - 日期格式
	 * @param date - 日期类型
	 * @return
	 */
	public static String convertDateToString(String mask, Date date) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (date != null) {
			df = new SimpleDateFormat(mask);
			returnValue = df.format(date);
		}
		return returnValue;
	}

	/**
	 * 字符串转日期，返回日期格式
	 *
	 * @param mask - 日期格式
	 * @param strDate - 字符串时间
	 * @return
	 */
	public static Date convertStringToDate(String mask, String strDate) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}
		Date date = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat(mask);
			date = df.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * compare 是否在star-end 之间，如果是返回true，否则返回false
	 *
	 * System.out.println(DateUtils.isInterval("0900", "1130", "1130", "hhmm"));
	 * System.out.println(DateUtils.isInterval("20151008", "20151010", "20151011", "yyyyMMdd"));
	 * System.out.println(DateUtils.isInterval("20151009113758", "20151009133758", "20151009143758", "yyyyMMdd"));
	 */
	public static Boolean isInterval(String star, String end, String compare, String formate) {
		Boolean isInterval = false;
		SimpleDateFormat localTime = new SimpleDateFormat(formate);
		try {
			Date sdate = localTime.parse(star);
			Date edate = localTime.parse(end);
			Date scompare = localTime.parse(compare);
			// Date1.after(Date2)，当Date1大于Date2时，返回true，当小于等于时，返回false；
			// Date1.before(Date2)，当Date1小于Date2时，返回true，当大于等于时，返回false；
			if (!scompare.after(edate) && !scompare.before(sdate)) {
				isInterval = true;
			} else {
				isInterval = false;
			}
		} catch (Exception e) {
			System.out.println("日期比较异常：" + e.getMessage());
			throw new RuntimeException("日期比较异常：" + e.getMessage());
		}
		return isInterval;
	}

	/**
	 * 获取笔记的起算时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getNoteStartTime(Date date) throws Exception {
		String timeString = START_TIME_09_20_00;
		String currentDate = convertDateToString("yyyy-MM-dd", date);
		String currentTime = convertDateToString("yyyy-MM-dd HH:mm:ss", date);
		if (DateUtil.isInterval(currentDate + " " + START_TIME_09_20_00, currentDate + " " + START_TIME_12_55_00, currentTime, "yyyy-MM-dd HH:mm:ss")) {
			timeString = START_TIME_12_55_00;
		}
		return timeString;
	}

}

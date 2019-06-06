package com.caimao.weixin.note.domain;

/**
 * 60分钟K线
 */
public class HourK {

	/** 股票代码 */
	private String code;

	/** 60分钟K线的更新时间 */
	private String day;

	/** 开盘价 */
	private String open;

	/** 最高价 */
	private String high;

	/** 最低价 */
	private String low;

	/** 上一次的收盘价 */
	private String close;

	/** 涨跌幅 */
	private float increase;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public float getIncrease() {
		return increase;
	}

	public void setIncrease(float increase) {
		this.increase = increase;
	}

}

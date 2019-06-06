package com.caimao.weixin.note.domain;

public class Ticker {

	/** 股票名称 */
	private String name;

	/** 股票代码 */
	private String code;

	/** 开盘价 */
	private String openPrice;

	/** 昨收价 */
	private String closePrice;

	/** 当前价 */
	private float currentPrice;

	/** 最高价 */
	private String highPrice;

	/** 最低价 */
	private String lowPrice;

	/** 成交数 */
	private String totalNum;

	/** 成交额 */
	private String totalMoney;

	/** 涨跌幅 */
	private float increase;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}

	public String getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
	}

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}

	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public float getIncrease() {
		return increase;
	}

	public void setIncrease(float increase) {
		this.increase = increase;
	}
}

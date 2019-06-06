package com.caimao.weixin.note.domain;

import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class Note extends BaseDomain {

	/** 主键ID */
	private int note_id;

	/** 用户ID */
	private int note_user_id;

	/** 股票代码 */
	@Length(min = 6, max = 6, message = "股票代码不正确")
	private String note_stock_code;

	/** 股票名称 */
	private String note_stock_name;

	/** 笔记股票开盘价 */
	private double note_stock_open_price;

	/** 笔记股票最高价 */
	private double note_stock_high_price;

	/** 目标天数 */
	private int note_target_day;

	/** 预期涨幅 */
	private int note_increase;
	
	/** 标题 */
	@Length(min = 0, max = 12)
	private String note_title;
	
	/** 笔记类型 */
	private int note_type;

	/** 投资逻辑 */
	@NotNull
	@Length(min = 5, max = 256, message = "字数必须在{min}-{max}之间")
	private String note_remark;

	/** 阅读费 */
	@NotNull
	@Digits(integer = 3, fraction = 2)
	//@DecimalMin(value = "0.01", message = "阅读费不得少于1分钱")
	@DecimalMax(value = "200.00", message = "阅读费不得超过200元")
	private double note_open_money;

	/** 红包费 */
	private double note_packet_money;

	/** 参与人数 */
	private int note_amount = 0;

	/** 笔记状态：0待开盘；1进行中；2已结束 */
	private int note_status = 0;

	/** 笔记状态：0未达标；1达标；2无效 */
	private int note_target_status = 0;

	/** 笔记支付状态：1未支付；2已支付 */
	private int note_pay_status = 1;

	/** 笔记保证金总额（TODO 未使用） */
	private double note_earnest_money;

	/** 开始时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date note_start_time = new Date();

	/** 结束时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date note_end_time = new Date();

	/** 创建时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date note_create_time;

	public int getNote_id() {
		return note_id;
	}

	public void setNote_id(int note_id) {
		this.note_id = note_id;
	}

	public int getNote_user_id() {
		return note_user_id;
	}

	public void setNote_user_id(int note_user_id) {
		this.note_user_id = note_user_id;
	}

	public String getNote_stock_code() {
		return note_stock_code;
	}

	public void setNote_stock_code(String note_stock_code) {
		this.note_stock_code = note_stock_code;
	}

	public String getNote_stock_name() {
		return note_stock_name;
	}

	public void setNote_stock_name(String note_stock_name) {
		this.note_stock_name = note_stock_name;
	}

	public double getNote_stock_open_price() {
		return note_stock_open_price;
	}

	public void setNote_stock_open_price(double note_stock_open_price) {
		this.note_stock_open_price = note_stock_open_price;
	}

	public double getNote_stock_high_price() {
		return note_stock_high_price;
	}

	public void setNote_stock_high_price(double note_stock_high_price) {
		this.note_stock_high_price = note_stock_high_price;
	}

	public int getNote_target_day() {
		return note_target_day;
	}

	public void setNote_target_day(int note_target_day) {
		this.note_target_day = note_target_day;
	}

	public int getNote_increase() {
		return note_increase;
	}

	public void setNote_increase(int note_increase) {
		this.note_increase = note_increase;
	}

	public String getNote_title() {
		return note_title;
	}

	public void setNote_title(String note_title) {
		this.note_title = note_title;
	}

	public int getNote_type() {
		return note_type;
	}

	public void setNote_type(int note_type) {
		this.note_type = note_type;
	}

	public String getNote_remark() {
		return note_remark;
	}

	public void setNote_remark(String note_remark) {
		this.note_remark = note_remark;
	}

	public double getNote_open_money() {
		return note_open_money;
	}

	public void setNote_open_money(double note_open_money) {
		this.note_open_money = note_open_money;
	}

	public double getNote_packet_money() {
		return note_packet_money;
	}

	public void setNote_packet_money(double note_packet_money) {
		this.note_packet_money = note_packet_money;
	}

	public int getNote_amount() {
		return note_amount;
	}

	public void setNote_amount(int note_amount) {
		this.note_amount = note_amount;
	}

	public int getNote_status() {
		return note_status;
	}

	public void setNote_status(int note_status) {
		this.note_status = note_status;
	}

	public int getNote_target_status() {
		return note_target_status;
	}

	public void setNote_target_status(int note_target_status) {
		this.note_target_status = note_target_status;
	}

	public int getNote_pay_status() {
		return note_pay_status;
	}

	public void setNote_pay_status(int note_pay_status) {
		this.note_pay_status = note_pay_status;
	}

	public double getNote_earnest_money() {
		return note_earnest_money;
	}

	public void setNote_earnest_money(double note_earnest_money) {
		this.note_earnest_money = note_earnest_money;
	}

	public Date getNote_start_time() {
		return note_start_time;
	}

	public void setNote_start_time(Date note_start_time) {
		this.note_start_time = note_start_time;
	}

	public Date getNote_end_time() {
		return note_end_time;
	}

	public void setNote_end_time(Date note_end_time) {
		this.note_end_time = note_end_time;
	}

	public Date getNote_create_time() {
		return note_create_time;
	}

	public void setNote_create_time(Date note_create_time) {
		this.note_create_time = note_create_time;
	}
}

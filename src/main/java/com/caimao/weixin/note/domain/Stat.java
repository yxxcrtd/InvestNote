package com.caimao.weixin.note.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Stat implements Serializable {

	/** 主键ID默认为1 */
	private int stat_id = 1;

	private int stat_user;

	private int stat_note;

	private int stat_reader;

	private int stat_ing;

	private int stat_success;

	private int stat_error;

	private int stat_view;

	private double stat_money;

	public int getStat_id() {
		return stat_id;
	}

	public void setStat_id(int stat_id) {
		this.stat_id = stat_id;
	}

	public int getStat_user() {
		return stat_user;
	}

	public void setStat_user(int stat_user) {
		this.stat_user = stat_user;
	}

	public int getStat_note() {
		return stat_note;
	}

	public void setStat_note(int stat_note) {
		this.stat_note = stat_note;
	}

	public int getStat_reader() {
		return stat_reader;
	}

	public void setStat_reader(int stat_reader) {
		this.stat_reader = stat_reader;
	}

	public int getStat_ing() {
		return stat_ing;
	}

	public void setStat_ing(int stat_ing) {
		this.stat_ing = stat_ing;
	}

	public int getStat_success() {
		return stat_success;
	}

	public void setStat_success(int stat_success) {
		this.stat_success = stat_success;
	}

	public int getStat_error() {
		return stat_error;
	}

	public void setStat_error(int stat_error) {
		this.stat_error = stat_error;
	}

	public int getStat_view() {
		return stat_view;
	}

	public void setStat_view(int stat_view) {
		this.stat_view = stat_view;
	}

	public double getStat_money() {
		return stat_money;
	}

	public void setStat_money(double stat_money) {
		this.stat_money = stat_money;
	}

}

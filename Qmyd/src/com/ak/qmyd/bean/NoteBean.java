package com.ak.qmyd.bean;

import com.google.gson.Gson;

public class NoteBean {
	private int id;
	private String time;
	private String title;
	private String day;
	private String way;
	private String count;
    private String week;
    private String timeType;
    private String index;
    private String isstart;
	public NoteBean(String time, String title, String day, String way,
			String count,String week,String timeType,String index,String isstart) {
		this.time = time;
		this.title = title;
		this.day = day;
		this.way = way;
		this.count = count;
		this.week = week;
		this.timeType = timeType;
		this.index = index;
		this.isstart = isstart;
	}

	public NoteBean(int id, String time, String title, String day, String way,
			String count,String week,String timeType,String index,String isstart) {
		this.id = id;
		this.time = time;
		this.title = title;
		this.day = day;
		this.way = way;
		this.count = count;
		this.week = week;
		this.timeType = timeType;
		this.index = index;
		this.isstart = isstart;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getIsstart() {
		return isstart;
	}

	public void setIsstart(String isstart) {
		this.isstart = isstart;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}

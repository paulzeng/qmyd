package com.ak.qmyd.bean;

import java.io.Serializable;


/** 
 * @author HM
 * @date 2015-5-20 ÏÂÎç3:17:05
 */
public class AlertDetailEntity implements Serializable{

	private String title;
	private String time;
	private String day;
	private String way;
	private String count;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
}

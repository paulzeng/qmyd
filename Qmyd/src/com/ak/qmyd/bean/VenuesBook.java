package com.ak.qmyd.bean;

import java.util.List;

public class VenuesBook {
		
	private String venuesDate;
	
	private String venuesId;
	
	private String venuesWeek;
	
	private List<BookDtail> reserveDetialList;
	
	private String defineId;
	
	//private int flag=1;//判断List<BookDtail>是否加载了数据

	public String getVenuesDate() {
		return venuesDate;
	}

	public void setVenuesDate(String venuesDate) {
		this.venuesDate = venuesDate;
	}

	public String getVenuesId() {
		return venuesId;
	}

	public void setVenuesId(String venuesId) {
		this.venuesId = venuesId;
	}

	public String getVenuesWeek() {
		return venuesWeek;
	}

	public void setVenuesWeek(String venuesWeek) {
		this.venuesWeek = venuesWeek;
	}

	public List<BookDtail> getReserveDetialList() {
		return reserveDetialList;
	}

	public void setReserveDetialList(List<BookDtail> reserveDetialList) {
		this.reserveDetialList = reserveDetialList;
	}

	public String getDefineId() {
		return defineId;
	}

	public void setDefineId(String defineId) {
		this.defineId = defineId;
	}

//	public int getFlag() {
//		return flag;
//	}
//
//	public void setFlag(int flag) {
//		this.flag = flag;
//	}

}

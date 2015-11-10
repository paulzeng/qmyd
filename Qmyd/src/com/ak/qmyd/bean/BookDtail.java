package com.ak.qmyd.bean;

public class BookDtail {
	
	
	String reserveMoney;
	
	String reserveSurplusNum;
	
	String reserveTimeSlot;
	
	int venuesLimit;
	
	String timeDefineId;
	
    int bookticket=0;;
	
	public String getReserveMoney() {
		return reserveMoney;
	}

	public void setReserveMoney(String reserveMoney) {
		this.reserveMoney = reserveMoney;
	}

	public String getReserveSurplusNum() {
		return reserveSurplusNum;
	}

	public void setReserveSurplusNum(String reserveSurplusNum) {
		this.reserveSurplusNum = reserveSurplusNum;
	}

	public String getReserveTimeSlot() {
		return reserveTimeSlot;
	}

	public void setReserveTimeSlot(String reserveTimeSlot) {
		this.reserveTimeSlot = reserveTimeSlot;
	}

	public int getBookticket() {
		return bookticket;
	}

	public void setBookticket(int bookticket) {
		this.bookticket = bookticket;
	}

	public int getVenuesLimit() {
		return venuesLimit;
	}

	public void setVenuesLimit(int venuesLimit) {
		this.venuesLimit = venuesLimit;
	}

	public String getTimeDefineId() {
		return timeDefineId;
	}

	public void setTimeDefineId(String timeDefineId) {
		this.timeDefineId = timeDefineId;
	}
	
}

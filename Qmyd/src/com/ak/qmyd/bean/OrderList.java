package com.ak.qmyd.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderList implements Serializable{
	
    
	private static final long serialVersionUID = 1L;
	
	String venuesDate;
	
	String reserveTimeSlot;
	
	String bookticket;
	
	String reserveMoney;
	
	String defineId; 
	
	String timeDefineId;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getVenuesDate() {
		return venuesDate;
	}

	public void setVenuesDate(String venuesDate) {
		this.venuesDate = venuesDate;
	}

	public String getReserveTimeSlot() {
		return reserveTimeSlot;
	}

	public void setReserveTimeSlot(String reserveTimeSlot) {
		this.reserveTimeSlot = reserveTimeSlot;
	}

	public String getBookticket() {
		return bookticket;
	}

	public void setBookticket(String bookticket) {
		this.bookticket = bookticket;
	}

	public String getReserveMoney() {
		return reserveMoney;
	}

	public void setReserveMoney(String reserveMoney) {
		this.reserveMoney = reserveMoney;
	}

	public String getDefineId() {
		return defineId;
	}

	public void setDefineId(String defineId) {
		this.defineId = defineId;
	}

	public String getTimeDefineId() {
		return timeDefineId;
	}

	public void setTimeDefineId(String timeDefineId) {
		this.timeDefineId = timeDefineId;
	}
	
	
}

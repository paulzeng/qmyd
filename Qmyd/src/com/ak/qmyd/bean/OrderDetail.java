package com.ak.qmyd.bean;

public class OrderDetail {

	private int orderDetailId;
	
	private String reserveDate;
	
	private String reserveTimeSlot;
	
	private String reserveType;
	
	private String reserveMoney;
	
	private String fieldName;
	
	private int reserveTypeId;
	
	private boolean flag=false;
	
	public String getReserveMoney() {
		return reserveMoney;
	}

	public void setReserveMoney(String reserveMoney) {
		this.reserveMoney = reserveMoney;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getReserveTypeId() {
		return reserveTypeId;
	}

	public void setReserveTypeId(int reserveTypeId) {
		this.reserveTypeId = reserveTypeId;
	}

	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}

	public String getReserveTimeSlot() {
		return reserveTimeSlot;
	}

	public void setReserveTimeSlot(String reserveTimeSlot) {
		this.reserveTimeSlot = reserveTimeSlot;
	}

	public String getReserveType() {
		return reserveType;
	}

	public void setReserveType(String reserveType) {
		this.reserveType = reserveType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}

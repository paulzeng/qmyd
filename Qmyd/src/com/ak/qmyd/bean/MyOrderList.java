package com.ak.qmyd.bean;

import java.util.List;

public class MyOrderList {

	private int orderId;
	
	private String orderCode;
	
	private String venuesThumbnail;
	
	private String venuesName;
	
	private String venuesAddr;
	
	private String orderType;
	
	private List<OrderDetail> orderDetailList;

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getVenuesThumbnail() {
		return venuesThumbnail;
	}

	public void setVenuesThumbnail(String venuesThumbnail) {
		this.venuesThumbnail = venuesThumbnail;
	}

	public String getVenuesName() {
		return venuesName;
	}

	public void setVenuesName(String venuesName) {
		this.venuesName = venuesName;
	}

	public String getVenuesAddr() {
		return venuesAddr;
	}

	public void setVenuesAddr(String venuesAddr) {
		this.venuesAddr = venuesAddr;
	}

	public List<OrderDetail> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<OrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}
}

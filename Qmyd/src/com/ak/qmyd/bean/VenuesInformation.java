package com.ak.qmyd.bean;

import java.util.List;

public class VenuesInformation {

	String venuesId;
	String venuesName;
	String venuesThumbnail;
	String venuesAddr;
	String venuesMobile;
	int supportId;
	String venuesContact;
	String venuesPhone;
	String venuesType;
	String venuesDistance;
	String venuesExplain;
	String pointX;
	String pointY;
	String venuesTotalScore;
	String venuesFavorNumber;
	String venuesBadNumber;
	String activityList;
	String reserveType;
	List<ServiceInformation> serviceList;

	List<VenuesDetailPicList> picList;

	public String getVenuesId() {
		return venuesId;
	}

	public String getReserveType() {
		return reserveType;
	}

	public void setReserveType(String reserveType) {
		this.reserveType = reserveType;
	}

	public void setVenuesId(String venuesId) {
		this.venuesId = venuesId;
	}

	public String getVenuesName() {
		return venuesName;
	}

	public void setVenuesName(String venuesName) {
		this.venuesName = venuesName;
	}

	public String getVenuesThumbnail() {
		return venuesThumbnail;
	}

	public void setVenuesThumbnail(String venuesThumbnail) {
		this.venuesThumbnail = venuesThumbnail;
	}

	public String getVenuesAddr() {
		return venuesAddr;
	}

	public void setVenuesAddr(String venuesAddr) {
		this.venuesAddr = venuesAddr;
	}

	public String getVenuesMobile() {
		return venuesMobile;
	}

	public void setVenuesMobile(String venuesMobile) {
		this.venuesMobile = venuesMobile;
	}

	public int getSupportId() {
		return supportId;
	}

	public void setSupportId(int supportId) {
		this.supportId = supportId;
	}

	public String getVenuesContact() {
		return venuesContact;
	}

	public void setVenuesContact(String venuesContact) {
		this.venuesContact = venuesContact;
	}

	public String getVenuesPhone() {
		return venuesPhone;
	}

	public void setVenuesPhone(String venuesPhone) {
		this.venuesPhone = venuesPhone;
	}

	public String getVenuesType() {
		return venuesType;
	}

	public void setVenuesType(String venuesType) {
		this.venuesType = venuesType;
	}

	public String getVenuesDistance() {
		return venuesDistance;
	}

	public void setVenuesDistance(String venuesDistance) {
		this.venuesDistance = venuesDistance;
	}

	public String getVenuesExplain() {
		return venuesExplain;
	}

	public void setVenuesExplain(String venuesExplain) {
		this.venuesExplain = venuesExplain;
	}

	public String getPointX() {
		return pointX;
	}

	public void setPointX(String pointX) {
		this.pointX = pointX;
	}

	public String getPointY() {
		return pointY;
	}

	public void setPointY(String pointY) {
		this.pointY = pointY;
	}

	public String getVenuesTotalScore() {
		return venuesTotalScore;
	}

	public void setVenuesTotalScore(String venuesTotalScore) {
		this.venuesTotalScore = venuesTotalScore;
	}

	public String getVenuesFavorNumber() {
		return venuesFavorNumber;
	}

	public void setVenuesFavorNumber(String venuesFavorNumber) {
		this.venuesFavorNumber = venuesFavorNumber;
	}

	public String getVenuesBadNumber() {
		return venuesBadNumber;
	}

	public void setVenuesBadNumber(String venuesBadNumber) {
		this.venuesBadNumber = venuesBadNumber;
	}

	public String getActivityList() {
		return activityList;
	}

	public void setActivityList(String activityList) {
		this.activityList = activityList;
	}

	public List<ServiceInformation> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<ServiceInformation> serviceList) {
		this.serviceList = serviceList;
	}

	public List<VenuesDetailPicList> getPicList() {
		return picList;
	}

	public void setPicList(List<VenuesDetailPicList> picList) {
		this.picList = picList;
	}

}

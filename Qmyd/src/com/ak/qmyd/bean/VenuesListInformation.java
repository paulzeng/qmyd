package com.ak.qmyd.bean;

import java.util.List;
import java.util.Map;

public class VenuesListInformation {
	
	String venuesId;
	String venuesName;
	String venuesThumbnail;
	String venuesAddr;
	String venuesDistance;
    List<Discount>  activityList;
    String supportId;
    
	public String getSupportId() {
		return supportId;
	}
	public void setSupportId(String supportId) {
		this.supportId = supportId;
	}
	public String getVenuesId() {
		return venuesId;
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

	public String getVenuesDistance() {
		return venuesDistance;
	}
	public void setVenuesDistance(String venuesDistance) {
		this.venuesDistance = venuesDistance;
	}
	public List<Discount> getActivityList() {
		return activityList;
	}
	public void setActivityList(List<Discount> activityList) {
		this.activityList = activityList;
	}
	
}

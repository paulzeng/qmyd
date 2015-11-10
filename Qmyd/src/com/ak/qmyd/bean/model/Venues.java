package com.ak.qmyd.bean.model;

import java.io.Serializable;
import java.util.List;

import com.ak.qmyd.config.Config;

/**
 * 场馆基本信息
 * 
 * @author yezilong
 *
 */
public class Venues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String venuesId; // 场馆ID
	private String venuesName; // 场馆名称
	private String venuesAddr; // 场馆地址
	private String venuesType; // 场馆类型
	private String venuesPhone; // 场馆电话
	private String venuesMobile; // 场馆手机
	private String venuesContact; // 场馆联系人
	private String venuesThumbnail; // 场馆缩略图
	private String venuesDistance; // 场馆距离
	private String supportId; //场地ID
	private String venuesExplain;//场地说明
	private Number venuesTotalScore; //场地总评分
	private Number venuesFavorNumber; //场地好评数
	private Number venuesBadNumber;  //场地差评数
	private List<VenuesServiceModel> serviceList;//服务类型
	private List<VenuesActivity> activityList;//场馆优惠
//	private List<VenuesReserve> ReserveList;//预约时间
	

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

	public String getVenuesAddr() {
		return venuesAddr;
	}

	public void setVenuesAddr(String venuesAddr) {
		this.venuesAddr = venuesAddr;
	}

	public String getVenuesType() {
		return venuesType;
	}

	public void setVenuesType(String venuesType) {
		this.venuesType = venuesType;
	}

	public String getVenuesPhone() {
		return venuesPhone;
	}

	public void setVenuesPhone(String venuesPhone) {
		this.venuesPhone = venuesPhone;
	}

	public String getVenuesMobile() {
		return venuesMobile;
	}

	public void setVenuesMobile(String venuesMobile) {
		this.venuesMobile = venuesMobile;
	}

	public String getVenuesContact() {
		return venuesContact;
	}

	public void setVenuesContact(String venuesContact) {
		this.venuesContact = venuesContact;
	}

	public String getVenuesThumbnail() {
		return Config.BASE_URL+venuesThumbnail;
	}

	public void setVenuesThumbnail(String venuesThumbnail) {
		this.venuesThumbnail = venuesThumbnail;
	}

	public String getVenuesDistance() {
		return venuesDistance;
	}

	public void setVenuesDistance(String venuesDistance) {
		this.venuesDistance = venuesDistance;
	}

	public List<VenuesServiceModel> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<VenuesServiceModel> serviceList) {
		this.serviceList = serviceList;
	}

	public List<VenuesActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<VenuesActivity> activityList) {
		this.activityList = activityList;
	}

	public String getVenuesExplain() {
		return venuesExplain;
	}

	public void setVenuesExplain(String venuesExplain) {
		this.venuesExplain = venuesExplain;
	}

	public Number getVenuesTotalScore() {
		return venuesTotalScore;
	}

	public void setVenuesTotalScore(Number venuesTotalScore) {
		this.venuesTotalScore = venuesTotalScore;
	}

	public Number getVenuesFavorNumber() {
		return venuesFavorNumber;
	}

	public void setVenuesFavorNumber(Number venuesFavorNumber) {
		this.venuesFavorNumber = venuesFavorNumber;
	}

	public Number getVenuesBadNumber() {
		return venuesBadNumber;
	}

	public void setVenuesBadNumber(Number venuesBadNumber) {
		this.venuesBadNumber = venuesBadNumber;
	}

	public String getSupportId() {
		return supportId;
	}

	public void setSupportId(String supportId) {
		this.supportId = supportId;
	}
	
}

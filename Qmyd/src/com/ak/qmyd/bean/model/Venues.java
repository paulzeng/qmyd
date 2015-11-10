package com.ak.qmyd.bean.model;

import java.io.Serializable;
import java.util.List;

import com.ak.qmyd.config.Config;

/**
 * ���ݻ�����Ϣ
 * 
 * @author yezilong
 *
 */
public class Venues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String venuesId; // ����ID
	private String venuesName; // ��������
	private String venuesAddr; // ���ݵ�ַ
	private String venuesType; // ��������
	private String venuesPhone; // ���ݵ绰
	private String venuesMobile; // �����ֻ�
	private String venuesContact; // ������ϵ��
	private String venuesThumbnail; // ��������ͼ
	private String venuesDistance; // ���ݾ���
	private String supportId; //����ID
	private String venuesExplain;//����˵��
	private Number venuesTotalScore; //����������
	private Number venuesFavorNumber; //���غ�����
	private Number venuesBadNumber;  //���ز�����
	private List<VenuesServiceModel> serviceList;//��������
	private List<VenuesActivity> activityList;//�����Ż�
//	private List<VenuesReserve> ReserveList;//ԤԼʱ��
	

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

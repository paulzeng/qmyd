package com.ak.qmyd.bean.result;

import java.io.Serializable;
import java.util.List;

import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.bean.model.Train;
import com.ak.qmyd.bean.model.Venues;
import com.google.gson.Gson;

public class HomeResult extends Result {

	private Train trainObject;
	private List<Venues> venuesArray;
	private List<DongTai> dongTaiList;
	private List<VenuesEventList> venuesEventList;

	public List<DongTai> getDongTaiList() {
		return dongTaiList;
	}

	public void setDongTaiList(List<DongTai> dongTaiList) {
		this.dongTaiList = dongTaiList;
	}

	public Train getTrainObject() {
		return trainObject;
	}

	public void setTrainObject(Train trainObject) {
		this.trainObject = trainObject;
	}

	public List<Venues> getVenuesArray() {
		return venuesArray;
	}

	public void setVenuesArray(List<Venues> venuesArray) {
		this.venuesArray = venuesArray;
	}

	public List<VenuesEventList> getVenuesEventList() {
		return venuesEventList;
	}

	public void setVenuesEventList(List<VenuesEventList> venuesEventList) {
		this.venuesEventList = venuesEventList;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

	public class VenuesEventList implements Serializable{
		private static final long serialVersionUID = 1L;
		private String content;
		private String eventAddress;
		private String eventBeginTime;
		private String eventEndTime;
		private String eventId;
		private String eventName;
		private String eventPic;;
		private String eventType;
		private String signBeginTime;
		private String signEndTime;
		private String sportsType;
		private String telePhone;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getEventAddress() {
			return eventAddress;
		}

		public void setEventAddress(String eventAddress) {
			this.eventAddress = eventAddress;
		}

		public String getEventBeginTime() {
			return eventBeginTime;
		}

		public void setEventBeginTime(String eventBeginTime) {
			this.eventBeginTime = eventBeginTime;
		}

		public String getEventEndTime() {
			return eventEndTime;
		}

		public void setEventEndTime(String eventEndTime) {
			this.eventEndTime = eventEndTime;
		}

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public String getEventPic() {
			return eventPic;
		}

		public void setEventPic(String eventPic) {
			this.eventPic = eventPic;
		}

		public String getEventType() {
			return eventType;
		}

		public void setEventType(String eventType) {
			this.eventType = eventType;
		}

		public String getSignBeginTime() {
			return signBeginTime;
		}

		public void setSignBeginTime(String signBeginTime) {
			this.signBeginTime = signBeginTime;
		}

		public String getSignEndTime() {
			return signEndTime;
		}

		public void setSignEndTime(String signEndTime) {
			this.signEndTime = signEndTime;
		}

		public String getSportsType() {
			return sportsType;
		}

		public void setSportsType(String sportsType) {
			this.sportsType = sportsType;
		}

		public String getTelePhone() {
			return telePhone;
		}

		public void setTelePhone(String telePhone) {
			this.telePhone = telePhone;
		}
	}

	public class DongTai {
		private String myInfoId, imagePath, userId;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getMyInfoId() {
			return myInfoId;
		}

		public void setMyInfoId(String myInfoId) {
			this.myInfoId = myInfoId;
		}

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

		public String toString() {
			return new Gson().toJson(this);
		}

	}
}

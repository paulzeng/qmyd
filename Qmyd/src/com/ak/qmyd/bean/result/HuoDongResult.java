package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-7-8 ÏÂÎç2:32:11
 */
public class HuoDongResult extends Result {
	private ArrayList<VenuesHotEventList> venuesHotEventList;
	private ArrayList<VenuesNewEventList> venuesNewEventList;

	public ArrayList<VenuesHotEventList> getVenuesHotEventList() {
		return venuesHotEventList;
	}

	public void setVenuesHotEventList(
			ArrayList<VenuesHotEventList> venuesHotEventList) {
		this.venuesHotEventList = venuesHotEventList;
	}

	public ArrayList<VenuesNewEventList> getVenuesNewEventList() {
		return venuesNewEventList;
	}

	public void setVenuesNewEventList(
			ArrayList<VenuesNewEventList> venuesNewEventList) {
		this.venuesNewEventList = venuesNewEventList;
	}

	public class VenuesHotEventList {
		private String content;
		private String eventAddress;
		private String eventBeginTime;
		private String eventEndTime;
		private String eventId;
		private String eventName;
		private String eventPic;
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

	public class VenuesNewEventList {

		private String content;
		private String eventAddress;
		private String eventBeginTime;
		private String eventEndTime;
		private String eventId;
		private String eventName;
		private String eventPic;
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
}

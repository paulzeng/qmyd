package com.ak.qmyd.bean;

import com.google.gson.Gson;

public class PushInfo {
	private int _id;
	private String name;
	private String id;
	private String url;
	private String pushInfo;
	private String time;
	private String status;
	private String type;
	private String img;
	private String userId;
	private String infoId;
	private String typeDetail;

	public PushInfo(int _id, String name, String id, String url,
			String pushInfo, String time, String status, String type,
			String img, String userId, String infoId, String typeDetail) {
		this._id = _id;
		this.name = name;
		this.id = id;
		this.url = url;
		this.pushInfo = pushInfo;
		this.time = time;
		this.status = status;
		this.type = type;
		this.img = img;
		this.userId = userId;
		this.infoId = infoId;
		this.typeDetail = typeDetail;
	}

	public String getTypeDetail() {
		return typeDetail;
	}

	public void setTypeDetail(String typeDetail) {
		this.typeDetail = typeDetail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPushInfo() {
		return pushInfo;
	}

	public void setPushInfo(String pushInfo) {
		this.pushInfo = pushInfo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

}

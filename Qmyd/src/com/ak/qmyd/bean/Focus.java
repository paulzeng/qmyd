package com.ak.qmyd.bean;

import java.util.List;

import com.google.gson.Gson;

public class Focus {
	private String userId, thumbnailPath, userName;
	private List<ImagePath> sportsTypeList;

	public Focus() {
	}

	public Focus(String userId, String thumbnailPath, String userName,
			List<ImagePath> sportsTypeList) {
		this.userId = userId;
		this.thumbnailPath = thumbnailPath;
		this.userName = userName;
		this.sportsTypeList = sportsTypeList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ImagePath> getSportsTypeList() {
		return sportsTypeList;
	}

	public void setSportsTypeList(List<ImagePath> sportsTypeList) {
		this.sportsTypeList = sportsTypeList;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

}

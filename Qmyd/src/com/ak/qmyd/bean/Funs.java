package com.ak.qmyd.bean;

import java.util.List;

import com.google.gson.Gson;

public class Funs {
	private String userId, userName, thumbnailPath, state;
	private List<ImagePath> sportsTypeList;

	public Funs(String userId, String userName, String thumbnailPath,
			String state, List<ImagePath> sportsTypeList) {
		this.userId = userId;
		this.userName = userName;
		this.thumbnailPath = thumbnailPath;
		this.state = state;
		this.sportsTypeList = sportsTypeList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

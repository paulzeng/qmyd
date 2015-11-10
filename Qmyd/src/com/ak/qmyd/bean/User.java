package com.ak.qmyd.bean;

import com.google.gson.Gson;

public class User {
	private String userId,userName,sex,height,weight,description,thumbnail,integral;

	public User(String userId, String userName, String sex, String height,
			String weight, String description, String thumbnail, String integral) {

		this.userId = userId;
		this.userName = userName;
		this.sex = sex;
		this.height = height;
		this.weight = weight;
		this.description = description;
		this.thumbnail = thumbnail;
		this.integral = integral;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String toString() {
		return new Gson().toJson(this);
	}
}

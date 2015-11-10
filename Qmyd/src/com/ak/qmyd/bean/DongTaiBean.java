package com.ak.qmyd.bean;

import com.google.gson.Gson;

/**
 * 动态-关注 实体
 * 
 * @author thomas
 * 
 */
public class DongTaiBean {
	private String userId;
	private String userName;
	private String myInfoId;
	private String content;
	private String imagePath;
	private String thumbnailPath;

	public DongTaiBean() {
	}

	public DongTaiBean(String userId, String userName, String myInfoId,
			String content, String imagePath, String thumbnailPath) {
		this.userId = userId;
		this.userName = userName;
		this.myInfoId = myInfoId;
		this.content = content;
		this.imagePath = imagePath;
		this.thumbnailPath = thumbnailPath;
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

	public String getMyInfoId() {
		return myInfoId;
	}

	public void setMyInfoId(String myInfoId) {
		this.myInfoId = myInfoId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}

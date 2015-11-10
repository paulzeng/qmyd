package com.ak.qmyd.bean;

import com.google.gson.Gson;

public class DongTaiContentBean {
	private String thumbnailPath,userName,userId,content,createTime;

	public DongTaiContentBean() {  
	}
	
	public DongTaiContentBean(String thumbnailPath, String userName,
			String userId, String content, String createTime) { 
		this.thumbnailPath = thumbnailPath;
		this.userName = userName;
		this.userId = userId;
		this.content = content;
		this.createTime = createTime;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String toString() {
		return new Gson().toJson(this);
	}
}

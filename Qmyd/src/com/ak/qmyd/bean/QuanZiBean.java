package com.ak.qmyd.bean;

import com.google.gson.Gson;

public class QuanZiBean {
	private String circleId, circleName, circleInfoId, infoTitle, imagePath,
			thumbnailPath;

	public QuanZiBean() {
	}

	public QuanZiBean(String circleId, String circleName, String circleInfoId,
			String infoTitle, String imagePath, String thumbnailPath) {
		this.circleId = circleId;
		this.circleName = circleName;
		this.circleInfoId = circleInfoId;
		this.infoTitle = infoTitle;
		this.imagePath = imagePath;
		this.thumbnailPath = thumbnailPath;
	}

	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getCircleInfoId() {
		return circleInfoId;
	}

	public void setCircleInfoId(String circleInfoId) {
		this.circleInfoId = circleInfoId;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
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

package com.ak.qmyd.bean.model;

import com.ak.qmyd.config.Config;
import com.google.gson.Gson;

public class Train {
	private String trainId;						//训练ID
	private String typeId;						//运动类型ID
	private String trainName;                //训练民称
	private String trainPerson;			    //训练制定人
	private String trainDifficulty;			//训练难度
	private String trainImage;				//训练缩略图首页
	private String trainImageDetail;		//训练缩略图详情
	private String description;				//训练简介
	private Number totalChapter;			//总章数
	
	public String getTrainId() {
		return trainId;
	}
	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public String getTrainPerson() {
		return trainPerson;
	}
	public void setTrainPerson(String trainPerson) {
		this.trainPerson = trainPerson;
	}
	

	public String getTrainImage() {
		return Config.BASE_URL+trainImage;
	}
	public void setTrainImage(String trainImage) {
		this.trainImage = trainImage;
	}	
	
	public String getTrainImageDetail() {
		return trainImageDetail;
	}
	public void setTrainImageDetail(String trainImageDetail) {
		this.trainImageDetail = trainImageDetail;
	}
	public String getTrainDifficulty() {
		return trainDifficulty;
	}
	public void setTrainDifficulty(String trainDifficulty) {
		this.trainDifficulty = trainDifficulty;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Number getTotalChapter() {
		return totalChapter;
	}
	public void setTotalChapter(Number totalChapter) {
		this.totalChapter = totalChapter;
	}
	public String toString() {
		return new Gson().toJson(this);
	}
}

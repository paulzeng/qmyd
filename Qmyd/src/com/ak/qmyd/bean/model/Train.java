package com.ak.qmyd.bean.model;

import com.ak.qmyd.config.Config;
import com.google.gson.Gson;

public class Train {
	private String trainId;						//ѵ��ID
	private String typeId;						//�˶�����ID
	private String trainName;                //ѵ�����
	private String trainPerson;			    //ѵ���ƶ���
	private String trainDifficulty;			//ѵ���Ѷ�
	private String trainImage;				//ѵ������ͼ��ҳ
	private String trainImageDetail;		//ѵ������ͼ����
	private String description;				//ѵ�����
	private Number totalChapter;			//������
	
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

package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-5-2 ÉÏÎç11:20:31
 */
public class TrainHistoryResult extends Result{

	private ArrayList<HistoryList> historyList;
	
	
	public ArrayList<HistoryList> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(ArrayList<HistoryList> historyList) {
		this.historyList = historyList;
	}

	public class HistoryList{
		private String trainMonth;
		private String trainTotalTime;
		private ArrayList<SectionArray> sectionArray;
		public String getTrainMonth() {
			return trainMonth;
		}
		public void setTrainMonth(String trainMonth) {
			this.trainMonth = trainMonth;
		}
		public String getTrainTotalTime() {
			return trainTotalTime;
		}
		public void setTrainTotalTime(String trainTotalTime) {
			this.trainTotalTime = trainTotalTime;
		}
		public ArrayList<SectionArray> getSectionArray() {
			return sectionArray;
		}
		public void setSectionArray(ArrayList<SectionArray> sectionArray) {
			this.sectionArray = sectionArray;
		}
	}
	
	public class SectionArray{
		private String sectionId;
		private String sectionName;
		private String sectionImage;
		private String sectionTotalTime;
		private String sectionTotalScore;
		private String trainMonth;
		
		public String getSectionId() {
			return sectionId;
		}
		public void setSectionId(String sectionId) {
			this.sectionId = sectionId;
		}
		public String getSectionName() {
			return sectionName;
		}
		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}
		public String getSectionImage() {
			return sectionImage;
		}
		public void setSectionImage(String sectionImage) {
			this.sectionImage = sectionImage;
		}
		public String getSectionTotalTime() {
			return sectionTotalTime;
		}
		public void setSectionTotalTime(String sectionTotalTime) {
			this.sectionTotalTime = sectionTotalTime;
		}
		public String getSectionTotalScore() {
			return sectionTotalScore;
		}
		public void setSectionTotalScore(String sectionTotalScore) {
			this.sectionTotalScore = sectionTotalScore;
		}
		public String getTrainMonth() {
			return trainMonth;
		}
		public void setTrainMonth(String trainMonth) {
			this.trainMonth = trainMonth;
		}
		
	}
	
}

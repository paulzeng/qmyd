package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-5-2 ÏÂÎç1:15:45
 */
public class TrainHistoryDetailResult extends Result{

	private ArrayList<SectionArray> sectionArray;

	public ArrayList<SectionArray> getSectionArray() {
		return sectionArray;
	}

	public void setSectionArray(ArrayList<SectionArray> sectionArray) {
		this.sectionArray = sectionArray;
	}
	
	public class SectionArray{
		private String 	sectionDate;
		private String sectionTime;
		private String sectionScore;
		public String getSectionDate() {
			return sectionDate;
		}
		public void setSectionDate(String sectionDate) {
			this.sectionDate = sectionDate;
		}
		public String getSectionTime() {
			return sectionTime;
		}
		public void setSectionTime(String sectionTime) {
			this.sectionTime = sectionTime;
		}
		public String getSectionScore() {
			return sectionScore;
		}
		public void setSectionScore(String sectionScore) {
			this.sectionScore = sectionScore;
		}
	}
}

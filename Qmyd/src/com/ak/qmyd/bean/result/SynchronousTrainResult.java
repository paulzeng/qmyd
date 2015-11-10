package com.ak.qmyd.bean.result;

import com.ak.qmyd.bean.model.Result;

public class SynchronousTrainResult extends Result{

	private SectionObject sectionObject;
	private String typeName;
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public SectionObject getSectionObject() {
		return sectionObject;
	}

	public void setSectionObject(SectionObject sectionObject) {
		this.sectionObject = sectionObject;
	}
	
	public class SectionObject{
		private String chapterId;
		private String sectionActionNum;
		private String  sectionCalorie;
		private String sectionId;
		private String sectionImage;
		private String sectionIntegral;
		private String sectionName;
		private String sectionLength;
		private String sectionVideoPath;
		public String getSectionIntegral() {
			return sectionIntegral;
		}
		public void setSectionIntegral(String sectionIntegral) {
			this.sectionIntegral = sectionIntegral;
		}
		public String getSectionName() {
			return sectionName;
		}
		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}
		
		public String getChapterId() {
			return chapterId;
		}
		public void setChapterId(String chapterId) {
			this.chapterId = chapterId;
		}
		public String getSectionActionNum() {
			return sectionActionNum;
		}
		public void setSectionActionNum(String sectionActionNum) {
			this.sectionActionNum = sectionActionNum;
		}
		public String getSectionCalorie() {
			return sectionCalorie;
		}
		public void setSectionCalorie(String sectionCalorie) {
			this.sectionCalorie = sectionCalorie;
		}
		public String getSectionId() {
			return sectionId;
		}
		public void setSectionId(String sectionId) {
			this.sectionId = sectionId;
		}
		public String getSectionImage() {
			return sectionImage;
		}
		public void setSectionImage(String sectionImage) {
			this.sectionImage = sectionImage;
		}
		public String getSectionLength() {
			return sectionLength;
		}
		public void setSectionLength(String sectionLength) {
			this.sectionLength = sectionLength;
		}
		public String getSectionVideoPath() {
			return sectionVideoPath;
		}
		public void setSectionVideoPath(String sectionVideoPath) {
			this.sectionVideoPath = sectionVideoPath;
		}
	}
}

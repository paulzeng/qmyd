package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

public class ChapterListResult extends Result{

	private ArrayList<SectionList> sectionList;
	private String chapterName;
	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public ArrayList<SectionList> getSectionList() {
		return sectionList;
	}

	public void setSectionList(ArrayList<SectionList> sectionList) {
		this.sectionList = sectionList;
	}
	

	public class SectionList{
		private String chapterId;
		private String sectionActionNum;
		private String sectionCalorie;
		private String sectionId;
		private String sectionName;
		private String sectionIntegral;
		private String sectionImage;
		private String sectionLength;
		private String sectionVideoPath;
		
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

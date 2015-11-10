package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

public class TrainListDetailResult extends Result{

	private ArrayList<ChapterList> chapterList;
	private Train train;
	
	
	
	public Train getTrain() {
		return train;
	}
	public void setTrain(Train train) {
		this.train = train;
	}
	public ArrayList<ChapterList> getChapterList() {
		return chapterList;
	}
	public void setChapterList(ArrayList<ChapterList> chapterList) {
		this.chapterList = chapterList;
	}
	public class Train{
		private String description;
		private int totalChapter;
		private String trainDifficulty;
		private String trainId;
		private String trainImage;
		private String trainImageDetail;
		private String trainName;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public int getTotalChapter() {
			return totalChapter;
		}
		public void setTotalChapter(int totalChapter) {
			this.totalChapter = totalChapter;
		}
		public String getTrainDifficulty() {
			return trainDifficulty;
		}
		public void setTrainDifficulty(String trainDifficulty) {
			this.trainDifficulty = trainDifficulty;
		}
		public String getTrainId() {
			return trainId;
		}
		public void setTrainId(String trainId) {
			this.trainId = trainId;
		}
		public String getTrainImage() {
			return trainImage;
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
		public String getTrainName() {
			return trainName;
		}
		public void setTrainName(String trainName) {
			this.trainName = trainName;
		}
		@Override
		public String toString() {
			return "Train [description=" + description + ", totalChapter="
					+ totalChapter + ", trainDifficulty=" + trainDifficulty
					+ ", trainId=" + trainId + ", trainImage=" + trainImage
					+ ", trainImageDetail=" + trainImageDetail + ", trainName="
					+ trainName + "]";
		}
		
	}
	
	
	public class ChapterList {

		private String chapterId;
		private String chapterName;
		private ArrayList<Sections> sections;

		public String getChapterId() {
			return chapterId;
		}

		public void setChapterId(String chapterId) {
			this.chapterId = chapterId;
		}

		public String getChapterName() {
			return chapterName;
		}

		public void setChapterName(String chapterName) {
			this.chapterName = chapterName;
		}

		public ArrayList<Sections> getSections() {
			return sections;
		}

		public void setSections(ArrayList<Sections> sections) {
			this.sections = sections;
		}

		public class Sections {
			private String sectionId;
			private String sectionImage;
			private String sectionLength;
			private String sectionName;

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

			public String getSectionName() {
				return sectionName;
			}

			public void setSectionName(String sectionName) {
				this.sectionName = sectionName;
			}
		}

	}

}

package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-6-11 ÏÂÎç8:22:54
 */
public class GoodNoteResult extends Result {

	private ArrayList<CircleDetailList> circleDetailList;

	public ArrayList<CircleDetailList> getCircleDetailList() {
		return circleDetailList;
	}

	public void setCircleDetailList(ArrayList<CircleDetailList> circleDetailList) {
		this.circleDetailList = circleDetailList;
	}

	public class CircleDetailList {
		private String circleInfoId;
		private String createTime;
		private String infoTitle;
		private String infoType;
		private String replayNum;
		private String thumbnailPath;
		private String userName;
		private String userId;

		public String getCircleInfoId() {
			return circleInfoId;
		}

		public void setCircleInfoId(String circleInfoId) {
			this.circleInfoId = circleInfoId;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getInfoTitle() {
			return infoTitle;
		}

		public void setInfoTitle(String infoTitle) {
			this.infoTitle = infoTitle;
		}

		public String getInfoType() {
			return infoType;
		}

		public void setInfoType(String infoType) {
			this.infoType = infoType;
		}

		public String getReplayNum() {
			return replayNum;
		}

		public void setReplayNum(String replayNum) {
			this.replayNum = replayNum;
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
	}
}

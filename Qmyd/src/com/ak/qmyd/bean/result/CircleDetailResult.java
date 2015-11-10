package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-6-11 ÏÂÎç4:22:52
 */
public class CircleDetailResult extends Result {

	private CircleDetail circleDetail;

	public CircleDetail getCircleDetail() {
		return circleDetail;
	}

	public void setCircleDetail(CircleDetail circleDetail) {
		this.circleDetail = circleDetail;
	}

	public class CircleDetail {
		private String circleId;
		private String circleName;
		private String circleNotice;
		private String thumbnailPath;
		private String state;
		private ArrayList<CircleDetailList> circleDetailList;

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

		public String getCircleNotice() {
			return circleNotice;
		}

		public void setCircleNotice(String circleNotice) {
			this.circleNotice = circleNotice;
		}

		
		public String getThumbnailPath() {
			return thumbnailPath;
		}

		public void setThumbnailPath(String thumbnailPath) {
			this.thumbnailPath = thumbnailPath;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public ArrayList<CircleDetailList> getCircleDetailList() {
			return circleDetailList;
		}

		public void setCircleDetailList(
				ArrayList<CircleDetailList> circleDetailList) {
			this.circleDetailList = circleDetailList;
		}

	}

	public class CircleDetailList {
		private String circleInfoId;
		private String createTime;
		private String infoTitle;
		private String infoType;
		private String replayNum;
		private String thumbnailPath;
		private String userId;
		private String userName;

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

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	}

}

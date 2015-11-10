package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-6-10 ÏÂÎç4:31:17
 */
public class CircleDongTaiResult extends Result {

	private CircleDongTai circleDongTai;

	public CircleDongTai getCircleDongTai() {
		return circleDongTai;
	}

	public void setCircleDongTai(CircleDongTai circleDongTai) {
		this.circleDongTai = circleDongTai;
	}

	public class CircleDongTai {
		private String userId;
		private String circleInfoId;
		private String userName;
		private String content;
		private String createTime;
		private String infoTitle;
		private String praiseNum;
		private String state;
		private String thumbnailPath;
		private String isDel;
		private ArrayList<CircleCommentList> circleCommentList;
		private ArrayList<CirclePicList> circlePicList;
		private ArrayList<CirclePraiseList> circlePraiseList;

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

		public String getIsDel() {
			return isDel;
		}

		public void setIsDel(String isDel) {
			this.isDel = isDel;
		}

		public String getCircleInfoId() {
			return circleInfoId;
		}

		public void setCircleInfoId(String circleInfoId) {
			this.circleInfoId = circleInfoId;
		}

		
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
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

		public String getPraiseNum() {
			return praiseNum;
		}

		public void setPraiseNum(String praiseNum) {
			this.praiseNum = praiseNum;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getThumbnailPath() {
			return thumbnailPath;
		}

		public void setThumbnailPath(String thumbnailPath) {
			this.thumbnailPath = thumbnailPath;
		}

		public ArrayList<CircleCommentList> getCircleCommentList() {
			return circleCommentList;
		}

		public void setCircleCommentList(
				ArrayList<CircleCommentList> circleCommentList) {
			this.circleCommentList = circleCommentList;
		}

		public ArrayList<CirclePicList> getCirclePicList() {
			return circlePicList;
		}

		public void setCirclePicList(ArrayList<CirclePicList> circlePicList) {
			this.circlePicList = circlePicList;
		}

		public ArrayList<CirclePraiseList> getCirclePraiseList() {
			return circlePraiseList;
		}

		public void setCirclePraiseList(
				ArrayList<CirclePraiseList> circlePraiseList) {
			this.circlePraiseList = circlePraiseList;
		}
	}

	public class CircleCommentList {
		private String commentId;
		private String content;
		private String createTime;
		private String isDel;
		private String thumbnailPath;
		private String userId;
		private String userName;

		public String getCommentId() {
			return commentId;
		}

		public void setCommentId(String commentId) {
			this.commentId = commentId;
		}

		public String getIsDel() {
			return isDel;
		}

		public void setIsDel(String isDel) {
			this.isDel = isDel;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
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

	public class CirclePicList {
		private String imagePath;

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

	}

	public class CirclePraiseList {
		private String thumbnailPath;
		private String userId;

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

	}
}

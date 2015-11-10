package com.ak.qmyd.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class StaffInfo {
	private String userName, thumbnailPath, createTime, myInfoId, content,
			replayNum, praiseNum, state, isDel;
	private ArrayList<ImagePath> infoPicList;
	private List<InfoPraise> infoPraiseList;
	private List<DongTaiContentBean> infoCommentList;

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMyInfoId() {
		return myInfoId;
	}

	public void setMyInfoId(String myInfoId) {
		this.myInfoId = myInfoId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReplayNum() {
		return replayNum;
	}

	public void setReplayNum(String replayNum) {
		this.replayNum = replayNum;
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

	public ArrayList<ImagePath> getInfoPicList() {
		return infoPicList;
	}

	public void setInfoPicList(ArrayList<ImagePath> infoPicList) {
		this.infoPicList = infoPicList;
	}

	public List<InfoPraise> getInfoPraiseList() {
		return infoPraiseList;
	}

	public void setInfoPraiseList(List<InfoPraise> infoPraiseList) {
		this.infoPraiseList = infoPraiseList;
	}

	public List<DongTaiContentBean> getInfoCommentList() {
		return infoCommentList;
	}

	public void setInfoCommentList(List<DongTaiContentBean> infoCommentList) {
		this.infoCommentList = infoCommentList;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
 

	public class InfoPraise {
		private String thumbnailPath, userId;

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

		public String toString() {
			return new Gson().toJson(this);
		}

	}

	public class InfoComment {
		private String thumbnailPath, createTime, content, userName, userId;

		public String getThumbnailPath() {
			return thumbnailPath;
		}

		public void setThumbnailPath(String thumbnailPath) {
			this.thumbnailPath = thumbnailPath;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
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

		public String toString() {
			return new Gson().toJson(this);
		}
	}
}

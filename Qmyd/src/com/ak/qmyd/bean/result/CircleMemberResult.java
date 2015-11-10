package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-6-11 ÏÂÎç7:32:03
 */
public class CircleMemberResult extends Result {

	private ArrayList<CircleUserList> circleUserList;

	public ArrayList<CircleUserList> getCircleUserList() {
		return circleUserList;
	}

	public void setCircleUserList(ArrayList<CircleUserList> circleUserList) {
		this.circleUserList = circleUserList;
	}

	public class CircleUserList {
		private String descirption;
		private String state;
		private String thumbnailPath;
		private String userId;
		private String userName;

		public String getDescirption() {
			return descirption;
		}

		public void setDescirption(String descirption) {
			this.descirption = descirption;
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

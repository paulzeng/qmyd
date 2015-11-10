package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-6-12 ÏÂÎç2:55:09
 */
public class ZanOrCancelZanResult extends Result {

	private PraiseCircleInfo praiseCircleInfo;

	public PraiseCircleInfo getPraiseCircleInfo() {
		return praiseCircleInfo;
	}

	public void setPraiseCircleInfo(PraiseCircleInfo praiseCircleInfo) {
		this.praiseCircleInfo = praiseCircleInfo;
	}

	public class PraiseCircleInfo {
		private String praiseNum;
		private String state;
		private ArrayList<CirclePraiseList> circlePraiseList;

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

		public ArrayList<CirclePraiseList> getCirclePraiseList() {
			return circlePraiseList;
		}

		public void setCirclePraiseList(
				ArrayList<CirclePraiseList> circlePraiseList) {
			this.circlePraiseList = circlePraiseList;
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

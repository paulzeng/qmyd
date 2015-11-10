package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-6-24 ÏÂÎç6:21:34
 */
public class HotCircleListResult extends Result{

	private ArrayList<HotCircleList> hotCircleList;
	
	public ArrayList<HotCircleList> getHotCircleList() {
		return hotCircleList;
	}

	public void setHotCircleList(ArrayList<HotCircleList> hotCircleList) {
		this.hotCircleList = hotCircleList;
	}

	public class HotCircleList{
		private String circleId;
		private String circleName;
		private String circleNotice;
		private String thunmnailPath;
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
		public String getThunmnailPath() {
			return thunmnailPath;
		}
		public void setThunmnailPath(String thunmnailPath) {
			this.thunmnailPath = thunmnailPath;
		}
	}
}

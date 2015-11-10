package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-6-11 ÏÂÎç3:25:43
 */
public class CircleListResult extends Result{

	private ArrayList<MyCircleList> myCircleList;
	private ArrayList<HotCircleList> hotCircleList;
	public ArrayList<MyCircleList> getMyCircleList() {
		return myCircleList;
	}
	public void setMyCircleList(ArrayList<MyCircleList> myCircleList) {
		this.myCircleList = myCircleList;
	}
	public ArrayList<HotCircleList> getHotCircleList() {
		return hotCircleList;
	}
	public void setHotCircleList(ArrayList<HotCircleList> hotCircleList) {
		this.hotCircleList = hotCircleList;
	}

	public class MyCircleList{
		private String circleId;
		private String thunmnailPath;
		public String getCircleId() {
			return circleId;
		}
		public void setCircleId(String circleId) {
			this.circleId = circleId;
		}
		public String getThunmnailPath() {
			return thunmnailPath;
		}
		public void setThunmnailPath(String thunmnailPath) {
			this.thunmnailPath = thunmnailPath;
		}
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

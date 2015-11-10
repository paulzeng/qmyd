package com.ak.qmyd.bean.result;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-5-14 ÏÂÎç3:45:25
 */
public class LoginResult extends Result{

	private String sessionId;
	private UserObject userObject;
	private int dynamic;
	private int fans;
	private int interest;
	
	public int getDynamic() {
		return dynamic;
	}
	public void setDynamic(int dynamic) {
		this.dynamic = dynamic;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	public int getInterest() {
		return interest;
	}
	public void setInterest(int interest) {
		this.interest = interest;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public UserObject getUserObject() {
		return userObject;
	}
	public void setUserObject(UserObject userObject) {
		this.userObject = userObject;
	}
	
	public class UserObject{
		private String userId;
		private String  userName;
		private String  sex;
		private String height;
		private String  weight;
		private String integral;
		private String  description;
		private String  thumbnail;
		private String sessionId;
		private int dynamic;
		private int fans;
		private int interest;
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public int getDynamic() {
			return dynamic;
		}
		public void setDynamic(int dynamic) {
			this.dynamic = dynamic;
		}
		public int getFans() {
			return fans;
		}
		public void setFans(int fans) {
			this.fans = fans;
		}
		public int getInterest() {
			return interest;
		}
		public void setInterest(int interest) {
			this.interest = interest;
		}
		public String getSessionId() {
			return sessionId;
		}
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getHeight() {
			return height;
		}
		public void setHeight(String height) {
			this.height = height;
		}
		public String getWeight() {
			return weight;
		}
		public void setWeight(String weight) {
			this.weight = weight;
		}
		public String getIntegral() {
			return integral;
		}
		public void setIntegral(String integral) {
			this.integral = integral;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getThumbnail() {
			return thumbnail;
		}
		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

	}
}

package com.ak.qmyd.bean;

public class Version {
	private String versionId;
	private String versionNum, versionName, appType, publishTime, filePath,
			netPath, appDesc, createTime, createStaff;
	
	public Version(String versionId, String versionNum, String versionName,
			String appType, String publishTime, String filePath,
			String netPath, String appDesc, String createTime,
			String createStaff) { 
		this.versionId = versionId;
		this.versionNum = versionNum;
		this.versionName = versionName;
		this.appType = appType;
		this.publishTime = publishTime;
		this.filePath = filePath;
		this.netPath = netPath;
		this.appDesc = appDesc;
		this.createTime = createTime;
		this.createStaff = createStaff;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getNetPath() {
		return netPath;
	}
	public void setNetPath(String netPath) {
		this.netPath = netPath;
	}
	public String getAppDesc() {
		return appDesc;
	}
	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateStaff() {
		return createStaff;
	}
	public void setCreateStaff(String createStaff) {
		this.createStaff = createStaff;
	}
	
}

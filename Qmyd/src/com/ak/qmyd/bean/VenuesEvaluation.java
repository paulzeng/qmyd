package com.ak.qmyd.bean;

import java.util.List;

public class VenuesEvaluation {
	
	double totalScore;
	int    appraiseTotalNum;
	int    favorNum;
	int    badNum;
	List<AppraiseDetail> appraiseDetailList;	
	
	public double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}
	public int getAppraiseTotalNum() {
		return appraiseTotalNum;
	}
	public void setAppraiseTotalNum(int appraiseTotalNum) {
		this.appraiseTotalNum = appraiseTotalNum;
	}
	public int getFavorNum() {
		return favorNum;
	}
	public void setFavorNum(int favorNum) {
		this.favorNum = favorNum;
	}
	public int getBadNum() {
		return badNum;
	}
	public void setBadNum(int badNum) {
		this.badNum = badNum;
	}
	public List<AppraiseDetail> getAppraiseDetailList() {
		return appraiseDetailList;
	}
	public void setAppraiseDetailList(List<AppraiseDetail> appraiseDetailList) {
		this.appraiseDetailList = appraiseDetailList;
	}
}

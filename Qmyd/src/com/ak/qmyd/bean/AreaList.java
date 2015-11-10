package com.ak.qmyd.bean;

import java.io.Serializable;

public class AreaList implements Serializable {

	String areaCode;
	
	String areaName;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}

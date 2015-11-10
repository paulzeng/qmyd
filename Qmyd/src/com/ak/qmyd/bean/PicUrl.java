package com.ak.qmyd.bean;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * @author HM
 * @date 2015-6-23 обнГ9:07:56
 */
public class PicUrl implements Serializable {

	private static final long serialVersionUID = 1L;

	private String picUrl;

	public PicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}

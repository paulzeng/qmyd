package com.ak.qmyd.bean;

import java.io.Serializable;

import com.google.gson.Gson;

public class ImagePath implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String imagePath;

	
	public ImagePath(String imagePath) { 
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}

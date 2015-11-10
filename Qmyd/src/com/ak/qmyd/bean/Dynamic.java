package com.ak.qmyd.bean;

/**
 * @author HM
 * @date 2015-4-18 ÏÂÎç2:49:28
 */
public class Dynamic {
	private int id;
	private String image;
	private String nickName;
	private String photo;
	private String title;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Dynamic(int id, String image, String nickName, String photo,
			String title) {
		super();
		this.id = id;
		this.image = image;
		this.nickName = nickName;
		this.photo = photo;
		this.title = title;
	}
	
}

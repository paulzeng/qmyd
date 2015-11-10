package com.ak.qmyd.bean;

public class Message {
	private String userHead;
	private String nickName;
	private String content;
	private String data;
	private boolean isRead;
	
	public Message(String userHead, String nickName, String content, String data,boolean isRead) { 
		this.userHead = userHead;
		this.nickName = nickName;
		this.content = content;
		this.data = data;
		this.isRead = isRead;
	}
	public String getUserHead() {
		return userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
}

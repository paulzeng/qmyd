package com.ak.qmyd.bean.model;

public class SportType {
	private String typeId;
	private String typeName;
	private String typeIndex;
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeIndex() {
		return typeIndex;
	}
	public void setTypeIndex(String typeIndex) {
		this.typeIndex = typeIndex;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return typeName+"  "+typeId;
	}
		
}

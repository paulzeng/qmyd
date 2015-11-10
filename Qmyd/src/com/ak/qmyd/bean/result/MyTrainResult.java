package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-5-19 ÏÂÎç6:37:51
 */
public class MyTrainResult extends Result{

	private ArrayList<TypeArray> typeArray;
	
	public ArrayList<TypeArray> getTypeArray() {
		return typeArray;
	}
	public void setTypeArray(ArrayList<TypeArray> typeArray) {
		this.typeArray = typeArray;
	}
	
	public class TypeArray{
		private String typeId;
		private String typeName;
		private String typeIndex;
		private String thumbnail;
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
		public String getThumbnail() {
			return thumbnail;
		}
		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
	}
}

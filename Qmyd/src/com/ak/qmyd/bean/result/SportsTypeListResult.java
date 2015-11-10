package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-6-16 ÏÂÎç2:46:08
 */
public class SportsTypeListResult extends Result {

	private ArrayList<SportsList> sportsList;

	public ArrayList<SportsList> getSportsList() {
		return sportsList;
	}

	public void setSportsList(ArrayList<SportsList> sportsList) {
		this.sportsList = sportsList;
	}

	public class SportsList {
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

	}
}

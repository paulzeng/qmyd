package com.ak.qmyd.bean.result;

import java.util.List;

import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.bean.model.SportType;

public class SportTypeResult extends Result{
	private List<SportType> typeArray;

	public List<SportType> getTypeArray() {
		return typeArray;
	}

	public void setTypeArray(List<SportType> typeArray) {
		this.typeArray = typeArray;
	}
	
}

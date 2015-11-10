package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.TraninListItemEntity;
import com.ak.qmyd.bean.model.Result;

public class TrainListResult extends Result{
	private ArrayList<TraninListItemEntity> trainList;

	public ArrayList<TraninListItemEntity> getTrainList() {
		return trainList;
	}

	public void setTrainList(ArrayList<TraninListItemEntity> trainList) {
		this.trainList = trainList;
	}

	
}

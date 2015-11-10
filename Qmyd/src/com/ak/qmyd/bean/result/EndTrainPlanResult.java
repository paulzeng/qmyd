package com.ak.qmyd.bean.result;

import com.ak.qmyd.bean.model.Result;

/**
 * @author JGB
 * @date 2015-5-3 ÏÂÎç5:35:14
 */
public class EndTrainPlanResult extends Result {

	private TrainObject trainObject;

	public TrainObject getTrainObject() {
		return trainObject;
	}

	public void setTrainObject(TrainObject trainObject) {
		this.trainObject = trainObject;
	}

	public class TrainObject {
		private String trainAction;
		private String trainHeat;
		private String trainScore;
		private String trainTime;

		public String getTrainAction() {
			return trainAction;
		}

		public void setTrainAction(String trainAction) {
			this.trainAction = trainAction;
		}

		public String getTrainHeat() {
			return trainHeat;
		}

		public void setTrainHeat(String trainHeat) {
			this.trainHeat = trainHeat;
		}

		public String getTrainScore() {
			return trainScore;
		}

		public void setTrainScore(String trainScore) {
			this.trainScore = trainScore;
		}

		public String getTrainTime() {
			return trainTime;
		}

		public void setTrainTime(String trainTime) {
			this.trainTime = trainTime;
		}

	}

}

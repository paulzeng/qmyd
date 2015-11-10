package com.ak.qmyd.bean.result;

import java.util.ArrayList;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-5-5 ÏÂÎç2:47:10
 */
public class TrainSkillResult extends Result{

	private ArrayList<TrainSkillList> trainSkillList;

	public ArrayList<TrainSkillList> getTrainSkillList() {
		return trainSkillList;
	}

	public void setTrainSkillList(ArrayList<TrainSkillList> trainSkillList) {
		this.trainSkillList = trainSkillList;
	}
	
	public class TrainSkillList{
		private String skillId;
		private String skillName;
		public String getSkillId() {
			return skillId;
		}
		public void setSkillId(String skillId) {
			this.skillId = skillId;
		}
		public String getSkillName() {
			return skillName;
		}
		public void setSkillName(String skillName) {
			this.skillName = skillName;
		}
		
	}
}

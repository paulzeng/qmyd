package com.ak.qmyd.bean.result;

import com.ak.qmyd.bean.model.Result;

/** 
 * @author JGB
 * @date 2015-5-5 ÏÂÎç3:14:44
 */
public class TrainSkillDetailResult extends Result{

	private Skill skill;
	
	
	
	public Skill getSkill() {
		return skill;
	}



	public void setSkill(Skill skill) {
		this.skill = skill;
	}



	public class Skill{
		private String skillConetent;
		private String skillId;
		private String skillName;
		public String getSkillConetent() {
			return skillConetent;
		}
		public void setSkillConetent(String skillConetent) {
			this.skillConetent = skillConetent;
		}
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

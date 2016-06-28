package forms;

import play.data.validation.Constraints.Required;

public class ProfessionSkillTopicSearchForm {
	@Required
	private Integer skillID;
	@Required
	private Integer p;

	public Integer getSkillID() {
		return skillID;
	}

	public void setSkillID(Integer skillID) {
		this.skillID = skillID;
	}

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}

}

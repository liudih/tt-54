package dto;

import entity.manager.ProfessionSkillTopic;

public class ProfessionSkillTopicDTO extends ProfessionSkillTopic {
	private String skillName;
	private String userName;
	private String languageName;

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

}

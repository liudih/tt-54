package forms;

import java.util.List;

public class UserSkillEditForm {
	private List<Integer> languageID;
	private List<Integer> professionID;
	private int userID;
	private int p;

	public List<Integer> getLanguageID() {
		return languageID;
	}

	public void setLanguageID(List<Integer> languageID) {
		this.languageID = languageID;
	}

	public List<Integer> getProfessionID() {
		return professionID;
	}

	public void setProfessionID(List<Integer> professionID) {
		this.professionID = professionID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	@Override
	public String toString() {
		return "UserSkillEditForm [languageID=" + languageID
				+ ", professionID=" + professionID + ", userID=" + userID
				+ ", p=" + p + "]";
	}

}

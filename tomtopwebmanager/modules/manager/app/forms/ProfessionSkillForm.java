package forms;

import play.data.validation.Constraints.Required;

public class ProfessionSkillForm {
	@Required
	private Integer p;
	@Required
	private Integer iid;
	@Required
	private String cskillname;

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCskillname() {
		return cskillname;
	}

	public void setCskillname(String cskillname) {
		this.cskillname = cskillname;
	}

}

package forms;

import play.data.validation.Constraints.Required;

public class ProfessionSkillTopicForm {
	@Required
	private Integer iskillid;
	@Required
	private String ctitle;
	@Required
	private String cdescription;
	@Required
	private Boolean benable;
	@Required
	private Integer ilanguageid;
	@Required
	private Integer p;
	private Integer iid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIskillid() {
		return iskillid;
	}

	public void setIskillid(Integer iskillid) {
		this.iskillid = iskillid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

	public Boolean getBenable() {
		return benable;
	}

	public void setBenable(Boolean benable) {
		this.benable = benable;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}

}

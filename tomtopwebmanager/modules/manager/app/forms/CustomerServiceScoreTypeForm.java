package forms;

import play.data.validation.Constraints.Required;

public class CustomerServiceScoreTypeForm {
	@Required
	private String cname;
	@Required
	private String cdescription;
	@Required
	private Integer p;
	@Required
	private Integer iid;

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

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

}

package entity.manager;

import java.util.Date;

public class ProfessionSkillTopic {
	private Integer iid;
	private Integer iskillid;
	private String ctitle;
	private String cdescription;
	private Boolean benable;
	private Integer icreateuser;
	private Date dcreatedate;
	private Integer ilanguageid;

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

	public Integer getIcreateuser() {
		return icreateuser;
	}

	public void setIcreateuser(Integer icreateuser) {
		this.icreateuser = icreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

}

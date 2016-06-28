package entity.manager;

import java.util.Date;

public class ProfessionSkill {
	private Integer iid;
	private String cskillname;
	private Date dcreatedate;

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

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}

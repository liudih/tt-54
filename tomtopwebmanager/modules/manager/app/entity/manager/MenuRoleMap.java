package entity.manager;

import java.util.Date;

public class MenuRoleMap {
	private Integer iid;
	private Integer imenuid;
	private Integer iroleid;
	private String ccreateuser;
	private Date dcreatedate;

	public Integer getImenuid() {
		return imenuid;
	}

	public void setImenuid(Integer imenuid) {
		this.imenuid = imenuid;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIroleid() {
		return iroleid;
	}

	public void setIroleid(Integer iroleid) {
		this.iroleid = iroleid;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}

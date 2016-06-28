package entity.manager;

import java.util.Date;

public class UserRoleMap {
	private Integer iid;
	private Integer iuserid;
	private Integer iroleid;
	private String ccreateuser;
	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIuserid() {
		return iuserid;
	}

	public void setIuserid(Integer iuserid) {
		this.iuserid = iuserid;
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

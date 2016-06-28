package entity.manager;

import java.util.Date;

public class AdminRole {
	public static final String SUPER_ADMIN = "Superadmin";
	private Integer iid;
	private String crolename;
	private String ccreateuser;
	private Date dcreatedate;

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCrolename() {
		return crolename;
	}

	public void setCrolename(String crolename) {
		this.crolename = crolename;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

}

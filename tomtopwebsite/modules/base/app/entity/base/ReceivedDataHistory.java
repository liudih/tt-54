package entity.base;

import java.util.Date;

public class ReceivedDataHistory {
	private Integer iid;

	private String ctype;

	private String ccontent;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public String getCtype() {
		return ctype;
	}

	public String getCcontent() {
		return ccontent;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
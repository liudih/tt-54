package dto.product;

import java.util.Date;

public class Attachment {
	private Integer iid;

	private String cdescribe;

	private String ctype;

	private String cfilename;

	private String cpath;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCdescribe() {
		return cdescribe;
	}

	public void setCdescribe(String cdescribe) {
		this.cdescribe = cdescribe == null ? null : cdescribe.trim();
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype == null ? null : ctype.trim();
	}

	public String getCfilename() {
		return cfilename;
	}

	public void setCfilename(String cfilename) {
		this.cfilename = cfilename == null ? null : cfilename.trim();
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath == null ? null : cpath.trim();
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
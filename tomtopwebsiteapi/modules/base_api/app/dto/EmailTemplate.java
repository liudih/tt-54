package dto;

import java.io.Serializable;
import java.util.Date;

public class EmailTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer iwebsiteid;

	private Integer ilanguage;

	private String ctype;

	private String ctitle;

	private String ccontent;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public Integer getIlanguage() {
		return ilanguage;
	}

	public String getCtype() {
		return ctype;
	}

	public String getCtitle() {
		return ctitle;
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

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public void setIlanguage(Integer ilanguage) {
		this.ilanguage = ilanguage;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
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

	public String getDate() {
		return dcreatedate.toString();
	}

}

package dto.product;

import java.io.Serializable;

public class CategoryLabel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer iwebsiteid;

	private String ctype;

	private Integer icategoryid;

	private String ccreateuser;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}
}
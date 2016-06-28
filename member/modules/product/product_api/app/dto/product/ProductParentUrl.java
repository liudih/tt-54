package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductParentUrl implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cparentsku;

	private Integer ilanguage;

	private String curl;

	private String ccreateuser;

	private Date dcreatedate;

	public String getCparentsku() {
		return cparentsku;
	}

	public void setCparentsku(String cparentsku) {
		this.cparentsku = cparentsku;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIlanguage() {
		return ilanguage;
	}

	public void setIlanguage(Integer ilanguage) {
		this.ilanguage = ilanguage;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl == null ? null : curl.trim();
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}
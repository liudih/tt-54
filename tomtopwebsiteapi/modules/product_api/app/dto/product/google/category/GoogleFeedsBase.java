package dto.product.google.category;

import java.io.Serializable;

public class GoogleFeedsBase implements Serializable {

	private static final long serialVersionUID = 5644563968279485577L;

	private Integer iid;
	private String country;
	private String clanguage;
	private String ccurrency;
	private Integer ilanguageid;
	private String ccreateuser;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getClanguage() {
		return clanguage;
	}

	public void setClanguage(String clanguage) {
		this.clanguage = clanguage;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

}

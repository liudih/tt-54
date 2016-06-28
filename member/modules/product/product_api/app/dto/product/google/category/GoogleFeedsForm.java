package dto.product.google.category;

import java.io.Serializable;

public class GoogleFeedsForm implements Serializable{

	private static final long serialVersionUID = 1530008279428513378L;
	
	private String country;
	private String clanguage;
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
	public Integer getLanguageid() {
		return languageid;
	}
	public void setLanguageid(Integer languageid) {
		this.languageid = languageid;
	}
	private String ccurrency;
	private Integer languageid;
}

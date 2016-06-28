package dto.product.google.category;

import java.io.Serializable;

public class GoogleAttributeMapperDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8916302285246893550L;
	private Integer icategoryid;
	private Integer iwebkeyid;
	private String ckeyname;
	private String ckeyValue;
	public Integer getIwebkeyid() {
		return iwebkeyid;
	}
	public void setIwebkeyid(Integer iwebkeyid) {
		this.iwebkeyid = iwebkeyid;
	}
	public Integer getIcategoryid() {
		return icategoryid;
	}
	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}
	public String getCkeyname() {
		return ckeyname;
	}
	public void setCkeyname(String ckeyname) {
		this.ckeyname = ckeyname;
	}
	public String getCkeyValue() {
		return ckeyValue;
	}
	public void setCkeyValue(String ckeyValue) {
		this.ckeyValue = ckeyValue;
	}

}

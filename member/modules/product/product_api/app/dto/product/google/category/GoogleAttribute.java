package dto.product.google.category;

import java.io.Serializable;

public class GoogleAttribute implements Serializable {

	private static final long serialVersionUID = -7124454483092821845L;

	private Integer icategoryid;
	private Integer iattributekeyid;
	private Integer iattributevalueid;
	private Integer iwebkeyid;

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public Integer getIattributekeyid() {
		return iattributekeyid;
	}

	public void setIattributekeyid(Integer iattributekeyid) {
		this.iattributekeyid = iattributekeyid;
	}

	public Integer getIattributevalueid() {
		return iattributevalueid;
	}

	public void setIattributevalueid(Integer iattributevalueid) {
		this.iattributevalueid = iattributevalueid;
	}

	public Integer getIwebkeyid() {
		return iwebkeyid;
	}

	public void setIwebkeyid(Integer iwebkeyid) {
		this.iwebkeyid = iwebkeyid;
	}

}

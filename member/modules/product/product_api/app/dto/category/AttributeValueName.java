package dto.category;

import java.io.Serializable;

public class AttributeValueName implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 自增长id
	 */
	Integer ikeyid;
	Integer ilanguageid;
	Integer ivalueid;
	String cvaluename;

	public Integer getIkeyid() {
		return ikeyid;
	}

	public void setIkeyid(Integer ikeyid) {
		this.ikeyid = ikeyid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public Integer getIvalueid() {
		return ivalueid;
	}

	public void setIvalueid(Integer ivalueid) {
		this.ivalueid = ivalueid;
	}

	public String getCvaluename() {
		return cvaluename;
	}

	public void setCvaluename(String cvaluename) {
		this.cvaluename = cvaluename;
	}
}

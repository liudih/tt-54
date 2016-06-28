package dto;

import java.io.Serializable;

public class AttributeValueItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer ilanguageid;

	private Integer ivalueid;

	private String cvaluename;

	private boolean notReal;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
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

	public boolean isNotReal() {
		return notReal;
	}

	public void setNotReal(boolean notReal) {
		this.notReal = notReal;
	}
}

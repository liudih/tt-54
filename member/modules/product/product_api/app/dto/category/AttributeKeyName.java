package dto.category;

import java.io.Serializable;

public class AttributeKeyName implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer iid;
	Integer ikeyid;
	Integer ilanguageid;
	String ckeyname;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

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

	public String getCkeyname() {
		return ckeyname;
	}

	public void setCkeyname(String ckeyname) {
		this.ckeyname = ckeyname;
	}

}

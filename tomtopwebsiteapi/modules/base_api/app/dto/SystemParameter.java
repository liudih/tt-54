package dto;

import java.io.Serializable;

public class SystemParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	Integer iwebsiteid;
	Integer ilanguageid;
	String cparameterkey;
	String cparametervalue;
	String languagename;
	String websitename;

	public String getWebsitename() {
		return websitename;
	}

	public void setWebsitename(String websitename) {
		this.websitename = websitename;
	}

	public String getLanguagename() {
		return languagename;
	}

	public void setLanguagename(String languagename) {
		this.languagename = languagename;
	}

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

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCparameterkey() {
		return cparameterkey;
	}

	public void setCparameterkey(String cparameterkey) {
		this.cparameterkey = cparameterkey;
	}

	public String getCparametervalue() {
		return cparametervalue;
	}

	public void setCparametervalue(String cparametervalue) {
		this.cparametervalue = cparametervalue;
	}

}

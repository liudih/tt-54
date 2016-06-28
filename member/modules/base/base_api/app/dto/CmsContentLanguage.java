package dto;

import java.io.Serializable;

public class CmsContentLanguage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iid;
	private Integer icmscontentid;
	private Integer ilanguageid;
	private String ckey;
	private String ctitle;
	private String ccontent;
	private String languagename;

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
	public Integer getIcmscontentid() {
		return icmscontentid;
	}
	public void setIcmscontentid(Integer icmscontentid) {
		this.icmscontentid = icmscontentid;
	}
	public Integer getIlanguageid() {
		return ilanguageid;
	}
	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}
	public String getCkey() {
		return ckey;
	}
	public void setCkey(String ckey) {
		this.ckey = ckey;
	}
	public String getCtitle() {
		return ctitle;
	}
	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}
	public String getCcontent() {
		return ccontent;
	}
	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	 
}

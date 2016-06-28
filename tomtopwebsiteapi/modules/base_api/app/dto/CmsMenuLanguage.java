package dto;

import java.io.Serializable;

public class CmsMenuLanguage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iid;
	private Integer imenuid;
	private Integer ilanguageid;
	private String cmenuname;
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

	public Integer getImenuid() {
		return imenuid;
	}

	public void setImenuid(Integer imenuid) {
		this.imenuid = imenuid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCmenuname() {
		return cmenuname;
	}

	public void setCmenuname(String cmenuname) {
		this.cmenuname = cmenuname;
	}

}

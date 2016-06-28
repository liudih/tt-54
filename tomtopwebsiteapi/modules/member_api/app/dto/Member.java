package dto;

import java.io.Serializable;

public class Member implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer iwebsiteid;

	private String caccount;

	private Integer igroupid;

	private String cprefix;

	private String csuffix;

	private String cfirstname;

	private String cmiddlename;

	private String clastname;

	private String cemail;

	private boolean bactivated;
    
	private String cactivationcode;
	
	private Boolean bsign;

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCaccount() {
		return caccount;
	}

	public void setCaccount(String caccount) {
		this.caccount = caccount;
	}

	public Integer getIgroupid() {
		return igroupid;
	}

	public void setIgroupid(Integer igroupid) {
		this.igroupid = igroupid;
	}

	public String getCprefix() {
		return cprefix;
	}

	public void setCprefix(String cprefix) {
		this.cprefix = cprefix;
	}

	public String getCsuffix() {
		return csuffix;
	}

	public void setCsuffix(String csuffix) {
		this.csuffix = csuffix;
	}

	public String getCfirstname() {
		return cfirstname;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}

	public String getCmiddlename() {
		return cmiddlename;
	}

	public void setCmiddlename(String cmiddlename) {
		this.cmiddlename = cmiddlename;
	}

	public String getClastname() {
		return clastname;
	}

	public void setClastname(String clastname) {
		this.clastname = clastname;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public boolean isBactivated() {
		return bactivated;
	}

	public void setBactivated(boolean bactivated) {
		this.bactivated = bactivated;
	}

	public String getCactivationcode() {
		return cactivationcode;
	}

	public void setCactivationcode(String cactivationcode) {
		this.cactivationcode = cactivationcode;
	}

	public Boolean getBsign() {
		return bsign;
	}

	public void setBsign(Boolean bsign) {
		this.bsign = bsign;
	}
	
	
}

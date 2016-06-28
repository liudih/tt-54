package com.rabbit.dto.category;

public class CategoryMessageForm {
	Integer iid;

	Integer icategoryid;

	Integer ilanguageid;

	String cname;

	String ctitle;

	String ckeywords;

	String cdescription;

	String cmetatitle;

	String cmetakeyword;

	String cmetadescription;

	String ccontent;

	byte[] cbackgroundimages;

	boolean bshow;

	Integer iposition;

	Integer icatetorywebsiteiid;

	Integer ibottom;

	Integer iright;

	Integer ibackgroundid;

	String curl;

	String cpath;

	Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCkeywords() {
		return ckeywords;
	}

	public void setCkeywords(String ckeywords) {
		this.ckeywords = ckeywords;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

	public String getCmetatitle() {
		return cmetatitle;
	}

	public void setCmetatitle(String cmetatitle) {
		this.cmetatitle = cmetatitle;
	}

	public String getCmetakeyword() {
		return cmetakeyword;
	}

	public void setCmetakeyword(String cmetakeyword) {
		this.cmetakeyword = cmetakeyword;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public byte[] getCbackgroundimages() {
		return cbackgroundimages;
	}

	public void setCbackgroundimages(byte[] cbackgroundimages) {
		this.cbackgroundimages = cbackgroundimages;
	}

	public String getCmetadescription() {
		return cmetadescription;
	}

	public void setCmetadescription(String cmetadescription) {
		this.cmetadescription = cmetadescription;
	}

	public boolean isBshow() {
		return bshow;
	}

	public void setBshow(boolean bshow) {
		this.bshow = bshow;
	}

	public Integer getIposition() {
		return iposition;
	}

	public void setIposition(Integer iposition) {
		this.iposition = iposition;
	}

	public Integer getIcatetorywebsiteiid() {
		return icatetorywebsiteiid;
	}

	public void setIcatetorywebsiteiid(Integer icatetorywebsiteiid) {
		this.icatetorywebsiteiid = icatetorywebsiteiid;
	}

	public Integer getIbottom() {
		return ibottom;
	}

	public void setIbottom(Integer ibottom) {
		this.ibottom = ibottom;
	}

	public Integer getIright() {
		return iright;
	}

	public void setIright(Integer iright) {
		this.iright = iright;
	}

	public Integer getIbackgroundid() {
		return ibackgroundid;
	}

	public void setIbackgroundid(Integer ibackgroundid) {
		this.ibackgroundid = ibackgroundid;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}

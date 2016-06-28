package dto.product;

import java.io.Serializable;

public class CategoryName implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer icategoryid;

	private Integer ilanguageid;

	private String cname;

	private String ctitle;

	private String ckeywords;

	private String cdescription;
	
	private String cmetatitle;
	
	private String cmetakeyword;
	
	private String cmetadescription;
	
	private String ccontent;
	
	private byte[] cbackgroundimages;
	
	private String cpath;
	
	private Integer iparentid;
	
	
	public Integer getIparentid() {
		return iparentid;
	}

	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

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
}
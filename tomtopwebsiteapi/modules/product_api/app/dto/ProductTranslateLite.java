package dto;

import java.io.Serializable;

public class ProductTranslateLite implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer ilanguageid;

	private String ctitle;

	private String cdescription;

	private String cshortdescription;

	private String cmetatitle;

	private String cmatekeyword;

	private String cmatedescription;

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

	public String getCshortdescription() {
		return cshortdescription;
	}

	public void setCshortdescription(String cshortdescription) {
		this.cshortdescription = cshortdescription;
	}

	public String getCmetatitle() {
		return cmetatitle;
	}

	public void setCmetatitle(String cmetatitle) {
		this.cmetatitle = cmetatitle;
	}

	public String getCmatekeyword() {
		return cmatekeyword;
	}

	public void setCmatekeyword(String cmatekeyword) {
		this.cmatekeyword = cmatekeyword;
	}

	public String getCmatedescription() {
		return cmatedescription;
	}

	public void setCmatedescription(String cmatedescription) {
		this.cmatedescription = cmatedescription;
	}

}

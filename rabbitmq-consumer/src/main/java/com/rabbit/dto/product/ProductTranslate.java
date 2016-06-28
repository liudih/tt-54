package com.rabbit.dto.product;

import java.io.Serializable;

public class ProductTranslate implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private String clistingid;

    private Integer ilanguageid;

    private String csku;

    private String ctitle;

    private String cdescription;

    private String cshortdescription;

    private String ckeyword;

    private String cmetatitle;

    private String cmetakeyword;

    private String cmetadescription;
    
    private String ccreateuser;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = clistingid == null ? null : clistingid.trim();
    }

    public Integer getIlanguageid() {
        return ilanguageid;
    }

    public void setIlanguageid(Integer ilanguageid) {
        this.ilanguageid = ilanguageid;
    }

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle == null ? null : ctitle.trim();
    }

    public String getCdescription() {
        return cdescription;
    }

    public void setCdescription(String cdescription) {
        this.cdescription = cdescription == null ? null : cdescription.trim();
    }

    public String getCshortdescription() {
        return cshortdescription;
    }

    public void setCshortdescription(String cshortdescription) {
        this.cshortdescription = cshortdescription == null ? null : cshortdescription.trim();
    }

    public String getCkeyword() {
        return ckeyword;
    }

    public void setCkeyword(String ckeyword) {
        this.ckeyword = ckeyword == null ? null : ckeyword.trim();
    }

    public String getCmetatitle() {
        return cmetatitle;
    }

    public void setCmetatitle(String cmetatitle) {
        this.cmetatitle = cmetatitle == null ? null : cmetatitle.trim();
    }

    public String getCmetakeyword() {
        return cmetakeyword;
    }

    public void setCmetakeyword(String cmetakeyword) {
        this.cmetakeyword = cmetakeyword == null ? null : cmetakeyword.trim();
    }

    public String getCmetadescription() {
        return cmetadescription;
    }

    public void setCmetadescription(String cmetadescription) {
        this.cmetadescription = cmetadescription == null ? null : cmetadescription.trim();
    }

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

}
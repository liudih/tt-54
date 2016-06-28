package com.rabbit.dto.product;

import java.io.Serializable;

public class ProductImage implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private String clistingid;

    private String csku;

    private String cimageurl;

    private String clabel;

    private Integer iorder;

    private Boolean bthumbnail;

    private Boolean bsmallimage;

    private Boolean bbaseimage;
    
    private String ccreateuser;
    
    private Boolean bshowondetails;//是否显示在产品详情页面
    
    private String coriginalurl; //原始url
    
    private Boolean bfetch; //是否已经抓取到erp图片

    public String getCoriginalurl() {
		return coriginalurl;
	}

	public void setCoriginalurl(String coriginalurl) {
		this.coriginalurl = coriginalurl;
	}

	public Boolean getBfetch() {
		return bfetch;
	}

	public void setBfetch(Boolean bfetch) {
		this.bfetch = bfetch;
	}

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

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }

    public String getCimageurl() {
        return cimageurl;
    }

    public void setCimageurl(String cimageurl) {
        this.cimageurl = cimageurl == null ? null : cimageurl.trim();
    }

    public String getClabel() {
        return clabel;
    }

    public void setClabel(String clabel) {
        this.clabel = clabel == null ? null : clabel.trim();
    }

    public Integer getIorder() {
        return iorder;
    }

    public void setIorder(Integer isortorder) {
        this.iorder = isortorder;
    }

    public Boolean getBthumbnail() {
        return bthumbnail;
    }

    public void setBthumbnail(Boolean bthumbnail) {
        this.bthumbnail = bthumbnail;
    }

    public Boolean getBsmallimage() {
        return bsmallimage;
    }

    public void setBsmallimage(Boolean bsmallimage) {
        this.bsmallimage = bsmallimage;
    }

    public Boolean getBbaseimage() {
        return bbaseimage;
    }

    public void setBbaseimage(Boolean bbaseimage) {
        this.bbaseimage = bbaseimage;
    }

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Boolean getBshowondetails() {
		return bshowondetails;
	}

	public void setBshowondetails(Boolean bshowondetails) {
		this.bshowondetails = bshowondetails;
	}
}
package dto;

import java.io.Serializable;

public class ProductImageLite implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cimagerurl;

	private String clabel;

	private Integer iorder;

	private Boolean bthumbnail;

	private Boolean bsmallimager;

	private Boolean bbaseimage;

	public String getCimagerurl() {
		return cimagerurl;
	}

	public void setCimagerurl(String cimagerurl) {
		this.cimagerurl = cimagerurl;
	}

	public String getClabel() {
		return clabel;
	}

	public void setClabel(String clabel) {
		this.clabel = clabel;
	}

	public Integer getIorder() {
		return iorder;
	}

	public void setIorder(Integer iorder) {
		this.iorder = iorder;
	}

	public Boolean getBthumbnail() {
		return bthumbnail;
	}

	public void setBthumbnail(Boolean bthumbnail) {
		this.bthumbnail = bthumbnail;
	}

	public Boolean getBsmallimager() {
		return bsmallimager;
	}

	public void setBsmallimager(Boolean bsmallimager) {
		this.bsmallimager = bsmallimager;
	}

	public Boolean getBbaseimage() {
		return bbaseimage;
	}

	public void setBbaseimage(Boolean bbaseimage) {
		this.bbaseimage = bbaseimage;
	}

}

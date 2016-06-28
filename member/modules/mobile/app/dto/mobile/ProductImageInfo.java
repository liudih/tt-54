package dto.mobile;

public class ProductImageInfo {

	private int iid;

	private String clistingid;

	private String csku;

	private String cimageurl;

	private String clabel;

	private int iorder;

	private boolean bthumbnail;

	private boolean bsmallimage;

	private boolean bbaseimage;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		if (null == iid) {
			iid = 0;
		}
		this.iid = iid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		if (null == clistingid) {
			clistingid = "";
		}
		this.clistingid = clistingid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		if (null == csku) {
			csku = "";
		}
		this.csku = csku;
	}

	public String getCimageurl() {
		return cimageurl;
	}

	public void setCimageurl(String cimageurl) {
		if (null == cimageurl) {
			cimageurl = "";
		}
		this.cimageurl = cimageurl;
	}

	public String getClabel() {
		return clabel;
	}

	public void setClabel(String clabel) {
		if (null == clabel) {
			clabel = "";
		}
		this.clabel = clabel;
	}

	public Integer getIorder() {
		return iorder;
	}

	public void setIorder(Integer iorder) {
		if (null == iorder) {
			iorder = 0;
		}
		this.iorder = iorder;
	}

	public Boolean getBthumbnail() {
		return bthumbnail;
	}

	public void setBthumbnail(Boolean bthumbnail) {
		if (null == bthumbnail) {
			bthumbnail = false;
		}
		this.bthumbnail = bthumbnail;
	}

	public Boolean getBsmallimage() {
		return bsmallimage;
	}

	public void setBsmallimage(Boolean bsmallimage) {
		if (null == bsmallimage) {
			bsmallimage = false;
		}
		this.bsmallimage = bsmallimage;
	}

	public Boolean getBbaseimage() {
		return bbaseimage;
	}

	public void setBbaseimage(Boolean bbaseimage) {
		if (null == bbaseimage) {
			bbaseimage = false;
		}
		this.bbaseimage = bbaseimage;
	}

}

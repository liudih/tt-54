package dto.category;

import java.io.Serializable;

public class CategoryBackgroundImages implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer icategorynameid;

	private Integer ibottom;

	private Integer iright;

	private byte[] cbackgroundimages;

	private String curl;

	private Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIcategorynameid() {
		return icategorynameid;
	}

	public void setIcategorynameid(Integer icategorynameid) {
		this.icategorynameid = icategorynameid;
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

	public byte[] getCbackgroundimages() {
		return cbackgroundimages;
	}

	public void setCbackgroundimages(byte[] cbackgroundimages) {
		this.cbackgroundimages = cbackgroundimages;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}
package dto.image;

import java.io.Serializable;

public class Img implements Serializable {

	private static final long serialVersionUID = 1L;

	long iid;
	byte[] bcontent;
	String cpath;
	String ccontenttype;
	String cmd5;
	String cdnpath;
	Integer iwebsiteid;

	public long getIid() {
		return iid;
	}

	public void setIid(long iid) {
		this.iid = iid;
	}

	public byte[] getBcontent() {
		return bcontent;
	}

	public void setBcontent(byte[] bcontent) {
		this.bcontent = bcontent;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public String getCcontenttype() {
		return ccontenttype;
	}

	public void setCcontenttype(String ccontenttype) {
		this.ccontenttype = ccontenttype;
	}

	public String getCmd5() {
		return cmd5;
	}

	public void setCmd5(String cmd5) {
		this.cmd5 = cmd5;
	}

	public String getCdnpath() {
		return cdnpath;
	}

	public void setCdnpath(String cdnpath) {
		this.cdnpath = cdnpath;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}

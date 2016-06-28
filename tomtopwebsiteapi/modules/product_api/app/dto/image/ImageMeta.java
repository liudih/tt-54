package dto.image;

import java.io.Serializable;

public class ImageMeta implements Serializable {

	private static final long serialVersionUID = 1L;

	long iid;
	String cpath;
	String cmd5;
	String contenttype;

	public ImageMeta(long iid, String cpath, String contenttype, String cmd5) {
		super();
		this.iid = iid;
		this.cpath = cpath;
		this.cmd5 = cmd5;
		this.contenttype = contenttype;
	}

	public ImageMeta() {
		super();
	}

	public long getIid() {
		return iid;
	}

	public void setIid(long iid) {
		this.iid = iid;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public String getCmd5() {
		return cmd5;
	}

	public void setCmd5(String cmd5) {
		this.cmd5 = cmd5;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

}

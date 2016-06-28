package com.rabbit.dto.image;

import java.io.Serializable;
import java.util.Date;

public class ImgUseMapping implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long iid;
	private Long iimgid;
	private String cpath;
	private String clabel;
	private String ccreateuser;
	private Date dcreatedate;

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

	public Long getIimgid() {
		return iimgid;
	}

	public void setIimgid(Long iimgid) {
		this.iimgid = iimgid;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public String getClabel() {
		return clabel;
	}

	public void setClabel(String clabel) {
		this.clabel = clabel;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}

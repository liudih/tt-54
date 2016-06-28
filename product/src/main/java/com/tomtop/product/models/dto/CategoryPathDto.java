package com.tomtop.product.models.dto;

import java.io.Serializable;

public class CategoryPathDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3546364788819450730L;
	private Integer iid;
	private String cpath;
	
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
}

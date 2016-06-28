package com.tomtop.product.models.dto.base;

import java.io.Serializable;

public class SimpleLanguage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cname;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public SimpleLanguage() {
		super();
	}

	public SimpleLanguage(Integer iid, String cname) {
		super();
		this.iid = iid;
		this.cname = cname;
	}

}
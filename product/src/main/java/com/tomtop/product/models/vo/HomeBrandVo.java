package com.tomtop.product.models.vo;

import java.io.Serializable;

public class HomeBrandVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9100090412374078005L;

	private String name;

	private String code;

	private String url;

	private String logoUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl == null ? null : logoUrl.trim();
	}
}
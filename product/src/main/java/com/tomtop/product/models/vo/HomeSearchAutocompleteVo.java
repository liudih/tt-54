package com.tomtop.product.models.vo;

import java.io.Serializable;

public class HomeSearchAutocompleteVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3685333816182870553L;
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword == null ? null : keyword.trim();
	}
}
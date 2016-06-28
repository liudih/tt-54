package com.tomtop.product.models.vo;

import java.io.Serializable;

public class HomeSearchKeywordVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6911019222883520124L;

	private Integer sort;

	private String keyword;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword == null ? null : keyword.trim();
	}
}
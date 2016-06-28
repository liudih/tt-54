package com.tomtop.product.models.vo;

import java.io.Serializable;

public class HomeFeaturedCategoryKeyVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 568598696754369510L;

	private String keyword;

	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer isort) {
		this.sort = isort;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String ckeyword) {
		this.keyword = ckeyword;
	}
}
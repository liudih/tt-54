package com.tomtop.product.models.vo;

import com.tomtop.framework.core.utils.Result;

public class ResultCategory extends Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1149938907440598632L;
	private Integer categoryId;
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	} 
}

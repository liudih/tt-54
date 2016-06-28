package com.tomtop.product.models.vo;

import java.io.Serializable;

public class HomeFeaturedCategoryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5670890359432774313L;

	private Integer id;

	private String imgUrl;

	private Integer number;

	private Integer sort;

	private String name;

	private Integer categoryId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer iid) {
		this.id = iid;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String cimgurl) {
		this.imgUrl = cimgurl == null ? null : cimgurl.trim();
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer inumber) {
		this.number = inumber;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer isort) {
		this.sort = isort;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
}
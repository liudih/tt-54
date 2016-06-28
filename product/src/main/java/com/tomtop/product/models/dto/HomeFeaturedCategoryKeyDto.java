package com.tomtop.product.models.dto;

import java.io.Serializable;

public class HomeFeaturedCategoryKeyDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5268458714633843927L;

	private Integer id;

	private Integer clientId;

	private Integer languageId;

	private Integer featuredCategoryId;

	private Integer isEnabled;

	private String keyword;

	private Integer sort;

	public Integer getId() {
		return id;
	}

	public void setId(Integer iid) {
		this.id = iid;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer iclientid) {
		this.clientId = iclientid;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer ilanguageid) {
		this.languageId = ilanguageid;
	}

	public Integer getFeaturedCategoryId() {
		return featuredCategoryId;
	}

	public void setFeaturedCategoryId(Integer ifcategoryid) {
		this.featuredCategoryId = ifcategoryid;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer istatus) {
		this.isEnabled = istatus;
	}

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
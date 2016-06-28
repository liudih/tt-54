package com.tomtop.product.models.bo;

import java.io.Serializable;

public class HomeFeaturedCategoryKeyBo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6254210356714120299L;

	private Integer id;

	private Integer clientId;

	private Integer languageId;

	private Integer featuredCategoryId;

	private String keyword;

	private Integer isEnabled;

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
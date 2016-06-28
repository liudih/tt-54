package com.tomtop.product.models.bo;

import java.io.Serializable;

/**
 * 首页特别类目内容bo
 * 
 * @author liulj
 *
 */
public class HomeFeaturedCategoryBo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8768710867079763211L;

	private Integer id;

	private String imgUrl;

	private Integer number;

	private Integer clientId;

	private Integer languageId;

	private Integer categoryId;

	private Integer sort;

	private Integer isEnabled;

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

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer icategoryid) {
		this.categoryId = icategoryid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer isort) {
		this.sort = isort;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer istatus) {
		this.isEnabled = istatus;
	}
}
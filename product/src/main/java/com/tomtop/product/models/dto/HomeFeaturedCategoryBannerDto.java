package com.tomtop.product.models.dto;

import java.io.Serializable;

public class HomeFeaturedCategoryBannerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2686045657953066308L;

	private Integer id;

	private Integer clientId;

	private Integer languageId;

	private Integer featuredCategoryId;

	private Integer positionId;

	private String url;

	private String imgUrl;

	private String title;

	private Integer sort;

	private Integer isEnabled;

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

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer ipositionid) {
		this.positionId = ipositionid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String curl) {
		this.url = curl == null ? null : curl.trim();
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String cimgurl) {
		this.imgUrl = cimgurl == null ? null : cimgurl.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String ctitle) {
		this.title = ctitle == null ? null : ctitle.trim();
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
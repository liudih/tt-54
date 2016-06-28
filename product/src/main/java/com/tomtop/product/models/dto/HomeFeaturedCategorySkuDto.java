package com.tomtop.product.models.dto;

import java.io.Serializable;

public class HomeFeaturedCategorySkuDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5717256766815633285L;

	private Integer id;

	private Integer clientId;

	private Integer languageId;

	private Integer featuredCategoryId;

	private String listingId;

	private String sku;

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

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String clistingid) {
		this.listingId = clistingid == null ? null : clistingid.trim();
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String csku) {
		this.sku = csku == null ? null : csku.trim();
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
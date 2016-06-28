package com.tomtop.product.models.vo;

import java.io.Serializable;

public class HomeFeaturedCategoryBannerVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8504504071990195682L;

	private Integer positionId;

	private String url;

	private String imgUrl;

	private String title;

	private Integer sort;

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
}
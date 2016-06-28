package com.tomtop.product.models.vo;

public class HomeFeaturedCategorySkuVo extends ProductBasePriceInfoVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 143439999633173476L;
	/**
	 * 排序号
	 */
	private Integer sort;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer isort) {
		this.sort = isort;
	}
}
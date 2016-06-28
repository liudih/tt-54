package com.tomtop.product.models.dto;

import org.apache.commons.lang3.StringUtils;

public class ProductBadge extends BaseDto {
	private static final long serialVersionUID = 1L;

	/**
	 * 标题
	 */
	String title;
	/**
	 * 路由地址
	 */
	String url;
	/**
	 * 图片地址
	 */
	String imageUrl;

	/**
	 * 商品评论条数
	 */
	private Integer reviewCount;

	/**
	 * 商品评分星级
	 */
	private Double avgScore;

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public Double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return StringUtils.isNotBlank(url) ? (this.url + ".html") : "";
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
}

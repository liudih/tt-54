package com.tomtop.product.models.vo;

/**
 * 产品基本信息和价格vo类
 * 
 * @author liulj
 *
 */
public class ProductBasePriceReviewInfoVo extends ProductBasePriceInfoVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5647011072728224578L;

	/**
	 * 商品评论条数
	 */
	private Integer reviewCount;

	/**
	 * 商品评分等级
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
}

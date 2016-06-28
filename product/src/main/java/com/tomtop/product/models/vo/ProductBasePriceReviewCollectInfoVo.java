package com.tomtop.product.models.vo;

/**
 * 产品基本信息和价格vo类
 * 
 * @author liulj
 *
 */
public class ProductBasePriceReviewCollectInfoVo extends
		ProductBasePriceReviewInfoVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1451172211513804491L;
	/**
	 * 收藏数
	 */
	private Integer collectNum;

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}
}

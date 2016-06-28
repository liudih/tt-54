package com.tomtop.product.models.bo;

/**
 * 产品基本信息和价格vo类
 * 
 * @author liulj
 *
 */
public class ProductBasePriceReviewCollectInfoBo extends
		ProductBasePriceReviewInfoBo {
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

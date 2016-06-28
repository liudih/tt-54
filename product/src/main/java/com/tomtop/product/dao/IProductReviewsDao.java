package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.ReviewCountAndScoreDto;
import com.tomtop.product.models.dto.ReviewTotalStartDto;

public interface IProductReviewsDao {
	/**
	 * 获取商品评伦，星级等信息
	 * 
	 * @param listingId
	 * @param istate
	 *            评论状态
	 * @return
	 */
	ReviewCountAndScoreDto getScoreByListingId(String listingId, int istate);

	/**
	 * 获取商品评伦，星级等信息,根具商品id列表
	 * 
	 * @param listingId
	 * @param istate
	 *            评论状态
	 * @return
	 */
	List<ReviewCountAndScoreDto> getScoreListByListingIds(
			List<String> listingId, int istate);
	
	List<ReviewTotalStartDto> getFoverallratingStartNumByListingId(String listingId, int istate);
}

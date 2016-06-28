package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.interaction.mappers.ProductReviewsMapper;
import com.tomtop.product.dao.IProductReviewsDao;
import com.tomtop.product.models.dto.ReviewCountAndScoreDto;
import com.tomtop.product.models.dto.ReviewTotalStartDto;

@Repository("productReviewsDao")
public class ProductReviewsDaoImpl implements IProductReviewsDao {

	@Autowired
	private ProductReviewsMapper mapper;

	@Override
	public ReviewCountAndScoreDto getScoreByListingId(String listingId,
			int istate) {
		return mapper.getScoreByListingId(listingId, istate);
	}

	@Override
	public List<ReviewCountAndScoreDto> getScoreListByListingIds(
			List<String> listingId, int istate) {
		return mapper.getScoreListByListingIds(listingId, istate);
	}

	@Override
	public List<ReviewTotalStartDto> getFoverallratingStartNumByListingId(
			String listingId, int istate) {
		return mapper.getFoverallratingNumByListingId(listingId, istate);
	}
}

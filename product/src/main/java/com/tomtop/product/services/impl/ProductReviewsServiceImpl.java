package com.tomtop.product.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.product.dao.IProductReviewsDao;
import com.tomtop.product.models.dto.ReviewCountAndScoreDto;
import com.tomtop.product.services.IProductReviewsService;

@Service("productReviewsService")
public class ProductReviewsServiceImpl implements IProductReviewsService {

	@Resource(name = "productReviewsDao")
	IProductReviewsDao productReviewsDao;

	@Override
	public Map<String, ReviewCountAndScoreDto> getAverageScores(
			List<String> listingIds, ReviewCountAndScoreDto defaultIfNotFound) {

		List<ReviewCountAndScoreDto> scores = productReviewsDao
				.getScoreListByListingIds(Lists.newArrayList(listingIds), 1);
		Map<String, ReviewCountAndScoreDto> exists = Maps.uniqueIndex(scores,
				s -> s.getListingId());
		return Maps.toMap(listingIds, (String id) -> {
			ReviewCountAndScoreDto score = exists.get(id);
			if (score != null) {
				return score;
			}
			return defaultIfNotFound;
		});
	}
}

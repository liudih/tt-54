package com.tomtop.product.services;

import java.util.List;
import java.util.Map;

import com.tomtop.product.models.dto.ReviewCountAndScoreDto;

public interface IProductReviewsService {
	public Map<String, ReviewCountAndScoreDto> getAverageScores(
			final List<String> listingIds,
			final ReviewCountAndScoreDto defaultIfNotFound);
}

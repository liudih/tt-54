package com.tomtop.product.services.review;

import java.util.List;

import com.tomtop.product.models.bo.ProductReviewBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;

public interface IInteractionCommentService {

	public ReviewStartNumBo getReviewStartNumBoById(String listingId);
	
	public List<ProductReviewBo> getProductReviewBoList(String listingId,Integer siteId);
}

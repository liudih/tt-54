package services.interaction.prodcut.fragment;

import java.util.Map;

import javax.inject.Inject;

import services.dodocool.product.IProductFragmentProvider;
import services.interaction.review.IProductReviewsService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class ProductReviewsStarFragmentProvider implements IProductFragmentProvider {

	@Inject
	IProductReviewsService productReviewsService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		String listingId = context.getListingID();
		Double averageScore = productReviewsService.getAverageScore(listingId);
		int reviewCount = productReviewsService.getReviewCount(listingId);
		int averageScoreStarWidth = 0;
		if (null != averageScore) {
			averageScoreStarWidth = (int) (Math.round((averageScore / 5) * 100));
		}
		return new valueobjects.interaction.Comment(listingId, null,
				averageScore, reviewCount, averageScoreStarWidth, null);
	}
}

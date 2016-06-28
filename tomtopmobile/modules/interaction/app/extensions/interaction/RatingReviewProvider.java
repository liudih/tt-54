package extensions.interaction;

import interceptors.CacheResult;

import java.util.Map;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.interaction.review.IProductReviewsService;
import valueobjects.interaction.ReviewCountAndScore;
import valueobjects.interaction.StarRatingParam;
import valueobjects.product.ProductBadgePartContext;

import com.google.common.collect.Maps;

public class RatingReviewProvider implements
		services.product.IProductBadgePartProvider {

	@Inject
	IProductReviewsService memberReviewService;

	@Override
	public String getName() {
		return "rating-review";
	}

	@Override
	@CacheResult("product.badges")
	public Map<String, Html> getFragment(ProductBadgePartContext partContext) {
		Map<String, ReviewCountAndScore> scores = memberReviewService
				.getAverageScores(partContext.getListingIds(),
						new ReviewCountAndScore(0, 0.0));
		return Maps
				.transformValues(
						scores,
						s -> {
							if (null != s && null != s.getListingId()) {
								String viewCommentUrl = s.getListingId();
								return views.html.interaction.fragment.product_star_amount_review.render(new StarRatingParam(
										(int) ((s.getAvgScore() * 0.1 / 5) * 1000),
										s.getReviewCount(), viewCommentUrl));
							} else {
								return views.html.interaction.fragment.product_star_amount_review
										.render(new StarRatingParam(0, 0, null));
							}
						});
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}
}

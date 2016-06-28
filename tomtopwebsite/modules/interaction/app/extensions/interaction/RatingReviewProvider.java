package extensions.interaction;

import interceptors.CacheResult;

import java.util.Map;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.interaciton.review.ProductReviewsService;
import valueobjects.interaction.ReviewCountAndScore;
import valueobjects.interaction.StarRatingParam;
import valueobjects.product.ProductBadgePartContext;

import com.google.common.collect.Maps;

public class RatingReviewProvider implements
		services.product.IProductBadgePartProvider {

	@Inject
	ProductReviewsService memberReviewService;

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
								String viewCommentUrl = controllers.interaction.review.routes.ProductReview
										.showAll(s.getListingId(), 1).url();
								return views.html.interaction.fragment.star_rating.render(new StarRatingParam(
										(int) ((s.getAvgScore() * 0.1 / 5) * 1000),
										s.getReviewCount(), viewCommentUrl));
							} else {
								return views.html.interaction.fragment.average_score_and_review_count.render(
										(int) ((s.getAvgScore() * 0.1 / 5) * 1000),
										s.getReviewCount());
							}
						});
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}
}

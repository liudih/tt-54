package services.research.fragment.provider;

import interceptors.CacheResult;

import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.twirl.api.Html;
import services.interaction.review.IProductReviewsService;
import valueobjects.interaction.ReviewCountAndScore;
import valueobjects.product.ProductBadgePartContext;

import com.google.common.collect.Maps;

public class ShowStarReviewProvider implements
		extensions.research.IProductBadgePartProvider {

	@Inject
	IProductReviewsService memberReviewService;

	@Override
	public String getName() {
		return "rating-review";
		
	}

	@Override
	//@CacheResult("product.badges")
	public Map<String, Html> getFragment(ProductBadgePartContext partContext) {
		Map<String, ReviewCountAndScore> scores = memberReviewService
				.getAverageScores(partContext.getListingIds(),
						new ReviewCountAndScore(0, 0.0));
Logger.debug("-------------------------------------------get--->review--");
		return Maps.transformValues(scores,
				s -> views.html.research.vote.show_review_star
						.render((int) ((s.getAvgScore() * 0.1 / 5) * 1000)));
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

}

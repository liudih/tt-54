package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class InteractionCommentReviewCountFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		Integer starsWidth = (Integer) context.getAttribute("starsWidth");
		Integer reviewCount = (Integer) context.getAttribute("reviewCount");
		return views.html.interaction.fragment.average_score_and_review_count
				.render(starsWidth, reviewCount);
	}
}

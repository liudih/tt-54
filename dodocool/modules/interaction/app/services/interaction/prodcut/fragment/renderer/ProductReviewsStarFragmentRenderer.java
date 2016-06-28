package services.interaction.prodcut.fragment.renderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.interaction.Comment;
import valueobjects.product.IProductFragment;

public class ProductReviewsStarFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.interaction.product.fragment.reviews_score_star_count
				.render((Comment) fragment);
	}
}

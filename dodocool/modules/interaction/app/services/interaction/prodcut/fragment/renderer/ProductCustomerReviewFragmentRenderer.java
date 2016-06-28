package services.interaction.prodcut.fragment.renderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

public class ProductCustomerReviewFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.interaction.product.fragment.product_reviews
				.render((valueobjects.dodocool.interaction.product.InteractionComment) fragment);
	}

}

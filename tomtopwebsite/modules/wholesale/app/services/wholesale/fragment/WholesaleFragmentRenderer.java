package services.wholesale.fragment;

import play.twirl.api.Html;
import services.cart.ICartFragmentRenderer;
import valueobjects.order_api.cart.CartRenderContext;
import valueobjects.order_api.cart.ICartFragment;

public class WholesaleFragmentRenderer implements ICartFragmentRenderer {

	@Override
	public Html render(ICartFragment fragment, CartRenderContext context) {
		return views.html.wholesale.product.wholesale_product_collect.render();
	}

}

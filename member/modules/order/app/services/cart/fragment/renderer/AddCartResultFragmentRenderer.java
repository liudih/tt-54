package services.cart.fragment.renderer;

import play.twirl.api.Html;
import services.cart.ICartFragmentRenderer;
import valueobjects.order_api.cart.CartRenderContext;
import valueobjects.order_api.cart.ICartFragment;

public class AddCartResultFragmentRenderer implements ICartFragmentRenderer {

	@Override
	public Html render(ICartFragment fragment, CartRenderContext context) {
		return views.html.cart.fragment.add_cart_result.render(context
				.getAttribute("result").toString());
	}

}

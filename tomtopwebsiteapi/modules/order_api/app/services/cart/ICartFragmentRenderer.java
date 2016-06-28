package services.cart;

import play.twirl.api.Html;
import valueobjects.order_api.cart.CartRenderContext;
import valueobjects.order_api.cart.ICartFragment;

public interface ICartFragmentRenderer {

	/**
	 * do the actual HTML rendering
	 *
	 * @param fragment
	 * @return
	 */
	Html render(ICartFragment fragment, CartRenderContext context);
}

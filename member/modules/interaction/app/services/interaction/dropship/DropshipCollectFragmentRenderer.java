package services.interaction.dropship;

import play.twirl.api.Html;
import services.cart.ICartFragmentRenderer;
import valueobjects.order_api.cart.CartRenderContext;
import valueobjects.order_api.cart.ICartFragment;

public class DropshipCollectFragmentRenderer implements ICartFragmentRenderer {

	@Override
	public Html render(ICartFragment fragment, CartRenderContext context) {
		return views.html.interaction.dropship.dropship_product_collect
				.render();
	}

}

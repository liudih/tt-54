package extensions.loyalty.cart;

import play.twirl.api.Html;
import services.cart.ICartFragmentRenderer;
import valueobjects.order_api.cart.CartRenderContext;
import valueobjects.order_api.cart.ICartFragment;

public class BulkPriceFragmentRenderer implements ICartFragmentRenderer {

	@Override
	public Html render(ICartFragment fragment, CartRenderContext context) {
		if (fragment != null) {
			BulkPriceCartFragment priceCart = (BulkPriceCartFragment) fragment;
			return views.html.loyalty.cart.fragment.bulk_price.render(priceCart
					.getGroup().getCgroupname(),
					priceCart.getFirst().getIqty(), priceCart.getRates(),
					priceCart.getPrices(), priceCart.getPrice().getCurrency());
		}
		return null;
	}

}

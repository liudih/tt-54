package services.dodocool.cart.fragmentRenderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

public class AddToCartFramentRenderer implements IProductFragmentRenderer {
	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		String listingId = (String) context.getAttribute("listingId");
		return views.html.cart.add_to_cart_menu.render(listingId);
	}
}
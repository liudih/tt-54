package services.cart.fragment.renderer;

import play.Logger;
import play.libs.Json;
import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class ProductBundleSaleFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.cart.product_bundle_sale
				.render((valueobjects.product.ProductBundleSale) fragment);
	}
}

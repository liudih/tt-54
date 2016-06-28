package services.cart.fragment.renderer;

import play.Configuration;
import play.Play;
import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;
import valueobjects.order_api.cart.ProductStorageMap;

public class ProductStorageMapFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.cart.fragment.product_storages
				.render((ProductStorageMap) fragment);
	}

}

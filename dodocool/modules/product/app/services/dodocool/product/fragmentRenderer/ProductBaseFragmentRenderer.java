package services.dodocool.product.fragmentRenderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

public class ProductBaseFragmentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.product.fragment.product_base.render(
				(valueobjects.product.ProductBase) fragment, context);
	}
}

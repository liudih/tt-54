package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;
import valueobjects.product.ProductSellingPoints;

public class ProductSellingPointsFragmentRender implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.product.fragment.product_selling_point
				.render((ProductSellingPoints) fragment);
	}

}

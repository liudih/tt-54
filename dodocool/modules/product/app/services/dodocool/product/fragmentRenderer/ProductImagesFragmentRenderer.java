package services.dodocool.product.fragmentRenderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

public class ProductImagesFragmentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		String title = (String) context.getAttribute("product-title");
		return views.html.product.fragment.product_images.render(
				(valueobjects.product.ProductImages) fragment, title);
	}
}

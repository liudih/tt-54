package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductImages;
import valueobjects.product.ProductRenderContext;

public class ProductImagesFragmentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.product.fragment.product_images.render(
				(ProductImages) fragment,
				(String) context.getAttribute("title"));
	}

}

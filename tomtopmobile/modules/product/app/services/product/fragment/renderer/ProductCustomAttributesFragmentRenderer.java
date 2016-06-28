package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductCustomAttributes;
import valueobjects.product.ProductRenderContext;

public class ProductCustomAttributesFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		/*if (fragment != null) {
			return views.html.product.fragment.product_custom_attributes
					.render((ProductCustomAttributes) fragment);
		}*/
		return null;
	}

}

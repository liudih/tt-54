package services.dodocool.product.fragmentRenderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import services.product.IEntityMapService;
import valueobjects.dodocool.product.ProductMultiAttribute;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

import com.google.inject.Inject;

public class ProductAttributeFragmentRenderer implements
		IProductFragmentRenderer {

	@Inject
	IEntityMapService entityMapService;

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.product.fragment.product_multiattribute
				.render((ProductMultiAttribute) fragment);
	}

}

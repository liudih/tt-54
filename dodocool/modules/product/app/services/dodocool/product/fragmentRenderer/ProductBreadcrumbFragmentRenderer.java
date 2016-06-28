package services.dodocool.product.fragmentRenderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;
import valueobjects.product.category.CategoryReverseComposite;

public class ProductBreadcrumbFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		CategoryReverseComposite rev = (CategoryReverseComposite) fragment;
		return views.html.product.fragment.breadcrumb.render(rev);
	}

}

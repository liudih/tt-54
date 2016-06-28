package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;
import valueobjects.product.category.CategoryReverseComposite;

public class ProductBreadcrumbFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		CategoryReverseComposite rev = (CategoryReverseComposite) fragment;
		return views.html.product.fragment.breadcrumb.render(rev);
	}

}

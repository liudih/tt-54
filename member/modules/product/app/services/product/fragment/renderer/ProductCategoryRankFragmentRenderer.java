package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.CategoryRankProductList;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class ProductCategoryRankFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.product.fragment.product_category_rank
				.render((CategoryRankProductList) fragment);
	}

}

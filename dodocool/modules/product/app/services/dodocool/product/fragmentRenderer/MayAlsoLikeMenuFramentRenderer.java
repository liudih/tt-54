package services.dodocool.product.fragmentRenderer;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

public class MayAlsoLikeMenuFramentRenderer implements IProductFragmentRenderer {
	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		boolean show = (boolean) context.getAttribute("show-also-like");
		return views.html.product.fragment.may_also_like_menu.render(show);
	}
}
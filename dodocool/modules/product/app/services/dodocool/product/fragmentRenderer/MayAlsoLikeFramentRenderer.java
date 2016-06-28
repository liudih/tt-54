package services.dodocool.product.fragmentRenderer;

import java.util.List;

import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;
import dto.product.ProductMessage;

public class MayAlsoLikeFramentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		List<ProductMessage> productMessages = (List) context
				.getAttribute("productMessages");
		return views.html.product.fragment.may_also_like
				.render(productMessages);
	}
}

package services.interaction.prodcut.fragment.renderer;

import play.twirl.api.Html;
import services.dodocool.base.FoundationService;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

import com.google.inject.Inject;

public class ProductAskQuestionFragmentRenderer implements
		IProductFragmentRenderer {

	@Inject
	FoundationService foundationService;

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		Boolean isLogin = foundationService.isLogined();
		String listingId = (String) context.getAttribute("listingId");
		String sku = (String) context.getAttribute("sku");
		return views.html.interaction.product.fragment.product_ask_question
				.render(listingId, sku, isLogin);
	}
}

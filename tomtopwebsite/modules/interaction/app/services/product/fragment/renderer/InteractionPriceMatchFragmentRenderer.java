package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.interaction.InteractionPriceMatchFragment;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class InteractionPriceMatchFragmentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		String listingid = (String)context.getAttribute("listingid");
		String sku = (String)context.getAttribute("sku");
		return views.html.interaction.fragment.price_match
				.render(listingid,sku);
	}

}

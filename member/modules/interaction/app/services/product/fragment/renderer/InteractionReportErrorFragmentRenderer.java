package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class InteractionReportErrorFragmentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		String listingid = (String)context.getAttribute("listingid");
		String sku = (String)context.getAttribute("sku");
		return views.html.interaction.fragment.report_error
				.render(listingid,sku);
	}

}

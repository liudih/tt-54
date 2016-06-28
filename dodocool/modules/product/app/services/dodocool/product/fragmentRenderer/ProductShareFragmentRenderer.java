package services.dodocool.product.fragmentRenderer;

import play.Configuration;
import play.Play;
import play.twirl.api.Html;
import services.dodocool.product.IProductFragmentRenderer;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

public class ProductShareFragmentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		Configuration config = Play.application().configuration()
				.getConfig("addthis");
		if (config != null && config.getString("pubid") != null) {
			String pid = config.getString("pubid");
			return views.html.product.fragment.product_share.render(pid);
		}
		return null;
	}
}

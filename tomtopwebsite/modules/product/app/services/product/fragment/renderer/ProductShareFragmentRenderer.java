package services.product.fragment.renderer;

import play.Configuration;
import play.Play;
import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

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

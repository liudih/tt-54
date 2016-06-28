package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;
import valueobjects.product.ProductVideo;

public class ProductVideoFragmentRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.product.fragment.product_videos
				.render((ProductVideo) fragment);
	}

}

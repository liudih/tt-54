package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class AdvertisingProductDetailFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		// TODO Auto-generated method stub
 		return views.html.product.fragment.advertising_productdetail
				.render((valueobjects.product.AdvertisingProductDetail) fragment);
	}

}

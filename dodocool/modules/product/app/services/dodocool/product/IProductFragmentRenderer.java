package services.dodocool.product;

import play.twirl.api.Html;
import valueobjects.dodocool.product.ProductRenderContext;
import valueobjects.product.IProductFragment;

public interface IProductFragmentRenderer {

	Html render(IProductFragment fragment, ProductRenderContext context);

}

package services.product;

import play.twirl.api.Html;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

/**
 * Implements how IProductFragment produces partial HTML
 *
 * @author kmtong
 *
 */
public interface IProductFragmentRenderer {

	/**
	 * do the actual HTML rendering
	 *
	 * @param fragment
	 * @param context
	 *            helpers to access other contents
	 * @return
	 */
	Html render(IProductFragment fragment, ProductRenderContext context);
}

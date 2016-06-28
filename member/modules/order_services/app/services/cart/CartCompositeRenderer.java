package services.cart;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.twirl.api.Html;
import valueobjects.order_api.cart.CartComposite;
import valueobjects.order_api.cart.CartRenderContext;
import valueobjects.order_api.cart.ICartFragment;

public class CartCompositeRenderer implements ICartCompositeRenderer {

	final Map<String, ICartFragmentRenderer> renderers;

	@Inject
	public CartCompositeRenderer(final Set<ICartFragmentPlugin> fragmentPlugins) {
		if (fragmentPlugins == null) {
			Logger.error("not found ICartFragmentPlugin!");
		}
		this.renderers = new HashMap<String, ICartFragmentRenderer>();
		for (ICartFragmentPlugin r : fragmentPlugins) {
			ICartFragmentRenderer renderer = r.getFragmentRenderer();
			if (renderer != null) {
				renderers.put(r.getName(), renderer);
			}
		}
	}

	/* (non-Javadoc)
	 * @see services.cart.ICartCompositeRenderer#renderFragment(valueobjects.order_api.cart.CartComposite, java.lang.String)
	 */
	@Override
	public Html renderFragment(CartComposite composite, String name) {
		ICartFragment fragment = composite.get(name);
		CartRenderContext ctx = new CartRenderContext(composite, this);
		ICartFragmentRenderer renderer = renderers.get(name);
		if (renderer != null) {
			return renderer.render(fragment, ctx);
		}
		return null;
	}
}

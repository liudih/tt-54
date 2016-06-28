package services.product;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.twirl.api.Html;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductComposite;
import valueobjects.product.ProductRenderContext;

public class ProductCompositeRenderer {

	final Map<String, IProductFragmentRenderer> renderers;

	@Inject
	public ProductCompositeRenderer(
			final Set<IProductFragmentPlugin> fragmentPlugins) {
		this.renderers = new HashMap<String, IProductFragmentRenderer>();
		for (IProductFragmentPlugin r : fragmentPlugins) {
			IProductFragmentRenderer renderer = r.getFragmentRenderer();
			if (renderer != null) {
				renderers.put(r.getName(), renderer);
			}
		}
	}

	public Html renderFragment(ProductComposite composite, String name) {
		IProductFragment fragment = composite.get(name);
		ProductRenderContext ctx = new ProductRenderContext(composite, this);
		IProductFragmentRenderer renderer = renderers.get(name);
		if (renderer != null) {
			return renderer.render(fragment, ctx);
		}
		return null;
	}
}

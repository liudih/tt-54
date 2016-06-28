package services.order;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.twirl.api.Html;
import valueobjects.order_api.ExistingOrderComposite;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderComposite;

public class OrderCompositeRenderer {

	final Map<String, IOrderFragmentRenderer> renderers;

	@Inject
	public OrderCompositeRenderer(
			final Set<IOrderFragmentPlugin> fragmentPlugins) {
		this.renderers = new HashMap<String, IOrderFragmentRenderer>();
		for (IOrderFragmentPlugin r : fragmentPlugins) {
			IOrderFragmentRenderer renderer = r.getFragmentRenderer();
			if (renderer != null) {
				renderers.put(r.getName(), renderer);
			}
		}
	}

	public Html renderFragment(OrderComposite composite, String name) {
		IOrderFragment fragment = composite.get(name);
		OrderRenderContext ctx = new OrderRenderContext(composite, this);
		IOrderFragmentRenderer renderer = renderers.get(name);
		if (renderer != null) {
			return renderer.render(fragment, ctx);
		}
		return null;
	}

	public Html renderFragment(ExistingOrderComposite composite, String name) {
		IOrderFragment fragment = composite.get(name);
		ExistingOrderRenderContext ctx = new ExistingOrderRenderContext(
				composite, this);
		if(composite.isConfirmView()){
			ctx.setConfirmView(true);
		}
		IOrderFragmentRenderer renderer = renderers.get(name);
		if (renderer != null) {
			return renderer.renderExisting(fragment, ctx);
		}
		return null;
	}
}

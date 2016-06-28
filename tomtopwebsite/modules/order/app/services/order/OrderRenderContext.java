package services.order;

import play.twirl.api.Html;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderComposite;

public class OrderRenderContext {

	OrderComposite composite;
	OrderCompositeRenderer renderer;

	public OrderRenderContext(OrderComposite composite,
			OrderCompositeRenderer renderer) {
		this.composite = composite;
		this.renderer = renderer;
	}

	public Html renderFragment(String name) {
		return renderer.renderFragment(composite, name);
	}

	public IOrderFragment getFragment(String name) {
		return composite.get(name);
	}

	public OrderComposite getComposite() {
		return composite;
	}

}

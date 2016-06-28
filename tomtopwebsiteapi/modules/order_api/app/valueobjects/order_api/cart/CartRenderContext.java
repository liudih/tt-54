package valueobjects.order_api.cart;

import java.io.Serializable;

import play.twirl.api.Html;
import services.cart.ICartCompositeRenderer;

public class CartRenderContext implements Serializable {
	private static final long serialVersionUID = 1L;
	CartComposite composite;
	ICartCompositeRenderer renderer;

	public CartRenderContext(CartComposite composite,
			ICartCompositeRenderer renderer) {
		super();
		this.composite = composite;
		this.renderer = renderer;
	}

	public Html renderFragment(String name) {
		return renderer.renderFragment(composite, name);
	}

	public ICartFragment getFragment(String name) {
		return composite.get(name);
	}

	public Object getAttribute(String name) {
		return composite.getAttributes().get(name);
	}
}

package valueobjects.dodocool.product;

import play.twirl.api.Html;
import services.dodocool.product.ProductCompositeRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductComposite;

public class ProductRenderContext {

	ProductComposite composite;
	ProductCompositeRenderer renderer;

	public ProductRenderContext(ProductComposite composite,
			ProductCompositeRenderer renderer) {
		super();
		this.composite = composite;
		this.renderer = renderer;
	}

	public Html renderFragment(String name) {
		return renderer.renderFragment(composite, name);
	}

	public IProductFragment getFragment(String name) {
		return composite.get(name);
	}

	public Object getAttribute(String name) {
		return composite.getAttributes().get(name);
	}

}

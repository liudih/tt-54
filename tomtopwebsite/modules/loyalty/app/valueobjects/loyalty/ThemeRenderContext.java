package valueobjects.loyalty;

import play.twirl.api.Html;
import services.loyalty.theme.ThemeCompositeRenderer;
import valueobjects.product.IProductFragment;

public class ThemeRenderContext {
	ThemeComposite composite;
	ThemeCompositeRenderer renderer;
	
	public ThemeRenderContext(ThemeComposite composite,ThemeCompositeRenderer renderer){
		super();
		this.composite = composite;
		this.renderer = renderer;
	}
	
	public Html renderFragment(String name) {
		return renderer.renderFragment(composite, name);
	}
	
	public IThemeFragment getFragment(String name) {
		return composite.get(name);
	}

	public Object getAttribute(String name) {
		return composite.getAttributes().get(name);
	}

}

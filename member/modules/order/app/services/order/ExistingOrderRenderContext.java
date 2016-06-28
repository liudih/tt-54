package services.order;

import play.twirl.api.Html;
import valueobjects.order_api.ExistingOrderComposite;
import valueobjects.order_api.IOrderFragment;

public class ExistingOrderRenderContext {

	ExistingOrderComposite composite;
	OrderCompositeRenderer renderer;
	// add by lijun
	// 是否是确认视图
	boolean isConfirmView = false;

	public ExistingOrderRenderContext(ExistingOrderComposite composite,
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

	public ExistingOrderComposite getComposite() {
		return composite;
	}

	public boolean isConfirmView() {
		return isConfirmView;
	}

	public void setConfirmView(boolean isConfirmView) {
		this.isConfirmView = isConfirmView;
	}

}

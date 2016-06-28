package services.mobile.cart.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class CartBarProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "cart-bar";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.mobile.cart.cart_icon.render();
	}

}

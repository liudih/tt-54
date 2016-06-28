package services.cart.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class CartDropFragmentProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "cart-dropdown";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.cart.cartdrop_new.render();
	}

}

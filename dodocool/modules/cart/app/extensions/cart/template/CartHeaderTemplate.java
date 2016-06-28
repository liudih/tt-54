package extensions.cart.template;

import java.util.List;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.dodocool.base.template.ITemplateFragmentProvider;
import services.dodocool.cart.CartService;
import valueobjects.order_api.cart.CartItem;

import com.google.inject.Inject;

public class CartHeaderTemplate implements ITemplateFragmentProvider {
	@Inject
	CartService cartService;

	@Override
	public String getName() {
		return "cart-header";
	}

	@Override
	public Html getFragment(Context context) {
		facades.cart.Cart cart = cartService.getCurrentCart(true);
		List<CartItem> list = cart.getAllItems();
		int count = null != list ? list.size() : 0;
		return views.html.cart.cart_menu.render(count);
	}

}

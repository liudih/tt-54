package services.cart;

import java.io.Serializable;

import valueobjects.cart.CartGetRequest;
import extensions.InjectorInstance;
import facade.cart.Cart;

public class CookieCartBuilderService implements ICartBuilderService, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Cart createCart(CartGetRequest getReq) {
		return createCartInstanceWithInjectedMembers(getReq);
	}

	private Cart createCartInstanceWithInjectedMembers(CartGetRequest getReq) {
		Cart cart = new Cart(getReq.getSiteID(), getReq.getLanguageID(), getReq.getCcy());
		InjectorInstance.getInjector().injectMembers(cart);
		return cart;
	}

}

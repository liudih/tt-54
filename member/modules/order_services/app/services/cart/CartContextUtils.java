package services.cart;

import services.IFoundationService;
import valueobjects.order_api.cart.CartContext;

public class CartContextUtils {

	public CartContext createCartContext(IFoundationService foundation,
			String listingID) {
		return new CartContext(foundation, listingID);
	}

	public CartContext createCartContext(IFoundationService foundation) {
		return new CartContext(foundation, null);
	}
}

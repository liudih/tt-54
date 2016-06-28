package valueobjects.order_api.cart;


import java.io.Serializable;

import valueobjects.product.IProductFragment;

public class CartAdder implements Serializable, IProductFragment {
	private static final long serialVersionUID = 1L;
	final CartItem cartItem;

	public CartAdder(CartItem cartItem) {
		this.cartItem = cartItem;
	}

	public CartItem getCartItem() {
		return this.cartItem;
	}
}

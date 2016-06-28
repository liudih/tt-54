package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;

import valueobjects.cart.CartItem;
import facades.cart.Cart;

public class Carts implements IOrderFragment, Serializable {
	Cart cart;
	//add by lijun
	List<CartItem> items;
	
	public Carts(Cart cart) {
		super();
		this.cart = cart;
	}
	public Carts(List<CartItem> items) {
		this.items = items;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public List<CartItem> getItems() {
		return items;
	}
}

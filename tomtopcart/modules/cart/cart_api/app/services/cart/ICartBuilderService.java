package services.cart;

import valueobjects.cart.CartGetRequest;
import facade.cart.Cart;

public interface ICartBuilderService {

	public Cart createCart(CartGetRequest getReq);

}
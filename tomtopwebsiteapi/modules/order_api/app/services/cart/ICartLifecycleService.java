package services.cart;

import valueobjects.order_api.cart.CartCreateRequest;
import valueobjects.order_api.cart.CartGetRequest;
import facades.cart.Cart;

public interface ICartLifecycleService {

	public abstract Cart createCart(CartCreateRequest createReq);

	public abstract Cart getCart(CartGetRequest getReq);

	public abstract Cart getOrCreateCart(CartGetRequest getReq);

	public abstract Cart getCart(String cartID);

	public abstract Cart getCart(String cartID, int siteID, int languageID,
			String ccy);

}
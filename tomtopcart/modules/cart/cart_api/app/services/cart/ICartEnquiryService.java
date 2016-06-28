package services.cart;

import java.util.List;

import valueobjects.cart.CartItem;

public interface ICartEnquiryService {

	public abstract List<CartItem> getCartItems(List<CartItem> items,
			int siteID, int languageID, String ccy);

	public abstract void addCartHistory(CartItem cartItem, Integer type);

	public abstract void addCartDelHistory(List<String> items);

	public abstract boolean isEnoughQty(CartItem cartItem);
}
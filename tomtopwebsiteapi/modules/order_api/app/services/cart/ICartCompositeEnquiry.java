package services.cart;

import valueobjects.order_api.cart.CartComposite;
import valueobjects.order_api.cart.CartContext;

public interface ICartCompositeEnquiry {

	public abstract CartComposite getCartComposite(CartContext Context);

}
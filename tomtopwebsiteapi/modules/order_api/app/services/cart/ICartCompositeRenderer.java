package services.cart;

import play.twirl.api.Html;
import valueobjects.order_api.cart.CartComposite;

public interface ICartCompositeRenderer {

	public abstract Html renderFragment(CartComposite composite, String name);

}
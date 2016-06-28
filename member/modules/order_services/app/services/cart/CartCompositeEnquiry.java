package services.cart;

import java.util.Set;

import javax.inject.Inject;

import valueobjects.order_api.cart.CartComposite;
import valueobjects.order_api.cart.CartContext;

public class CartCompositeEnquiry implements ICartCompositeEnquiry {
	@Inject
	Set<ICartFragmentPlugin> fragmentPlugins;

	/* (non-Javadoc)
	 * @see services.cart.ICartCompositeEnquiry#getCartComposite(valueobjects.order_api.cart.CartContext)
	 */
	@Override
	public CartComposite getCartComposite(CartContext Context) {
		CartComposite composite = new CartComposite(Context);
		for (ICartFragmentPlugin fp : fragmentPlugins) {
			ICartFragmentProvider provider = fp.getFragmentProvider();
			if (provider != null) {
				composite.put(fp.getName(), provider.getFragment(Context,
						composite.getAttributes()));
			}
		}
		return composite;
	}
}

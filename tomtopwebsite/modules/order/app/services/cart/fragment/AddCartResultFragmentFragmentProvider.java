package services.cart.fragment;

import java.util.Map;

import services.cart.ICartFragmentProvider;
import valueobjects.order_api.cart.CartContext;
import valueobjects.order_api.cart.ICartFragment;

public class AddCartResultFragmentFragmentProvider implements
		ICartFragmentProvider {

	@Override
	public ICartFragment getFragment(CartContext context,
			Map<String, Object> processingContext) {
		processingContext.put("result", "successfull");
		return null;
	}

}

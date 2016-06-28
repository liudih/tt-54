package services.dodocool.cart.fragment;

import java.util.Map;

import services.dodocool.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class AddToCartFragmentProvider implements IProductFragmentProvider {

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		return null;
	}

}

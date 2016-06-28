package services.cart.fragment;

import java.util.Map;

import javax.inject.Inject;

import services.member.login.LoginService;
import services.price.PriceService;
import services.product.IProductFragmentProvider;
import valueobjects.order_api.cart.CartAdder;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;

public class CartAddFragmentFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	PriceService priceService;

	@Inject
	LoginService loginService;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		SingleCartItem singleCartItem = new SingleCartItem();
		String clistingid = context.getListingID();
		IProductSpec spec = ProductSpecBuilder.build(clistingid).get();
		singleCartItem.setPrice(priceService.getPrice(spec));
		singleCartItem.setClistingid(context.getListingID());

		return new CartAdder(singleCartItem);
	}

}

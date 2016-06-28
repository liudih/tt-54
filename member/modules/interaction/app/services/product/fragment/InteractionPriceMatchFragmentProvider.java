package services.product.fragment;

import java.util.Map;

import services.product.IProductFragmentProvider;
import valueobjects.interaction.InteractionPriceMatchFragment;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class InteractionPriceMatchFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "price_match";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		String listingid = context.getListingID();
		String sku = context.getSku();
		return new InteractionPriceMatchFragment(listingid,sku,"");
	}

}

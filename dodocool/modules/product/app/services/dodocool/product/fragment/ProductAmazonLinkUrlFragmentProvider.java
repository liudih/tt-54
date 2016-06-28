package services.dodocool.product.fragment;

import java.util.Map;

import javax.inject.Inject;

import services.dodocool.product.IProductFragmentProvider;
import services.dodocool.product.ProductAmazonUrlService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductAmazonUrl;
import valueobjects.product.ProductContext;

public class ProductAmazonLinkUrlFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	ProductAmazonUrlService productAmazonUrlService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		Map<String, String> countryNameAndUrlMap = productAmazonUrlService
				.getProductAmazonUrl("amazon", context.getSku());
		return new ProductAmazonUrl(countryNameAndUrlMap);
	}
}

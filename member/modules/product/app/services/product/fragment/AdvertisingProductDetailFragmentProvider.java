package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.base.FoundationService;
import services.product.IProductFragmentProvider;
import services.product.ProductAdvertisingCompositeEnquiry;
import valueobjects.product.AdItem;
import valueobjects.product.AdvertisingProductDetail;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductAdertisingContext;
import valueobjects.product.ProductContext;

public class AdvertisingProductDetailFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	ProductAdvertisingCompositeEnquiry productAdvertisingService;
	
	@Inject
	FoundationService foundation;
	
	@Override
	public String getName() {
		return "product-detail-advertising";
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(
				context.getSku(), 1, foundation.getSiteID(),
				foundation.getLanguage(), 5, foundation.getDevice());

		List<AdItem> advertisingList = productAdvertisingService
								.getAdvertisings(pac);
		
 		return new AdvertisingProductDetail(advertisingList);
	}
}
	
 

package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dto.product.ProductBase;
import dto.product.ProductRecommend;
import services.base.FoundationService;
import services.product.IProductBadgeService;
import services.product.IProductEnquiryService;
import services.product.IProductFragmentProvider;
import services.product.IProductRecommendService;
import services.product.ProductUtilService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductContext;
import valueobjects.product.Recommendation;

public class ProductRecommendFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	IProductBadgeService badgeService;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	IProductRecommendService productRecommendService;

	@Inject
	FoundationService foundation;

	@Inject
	ProductUtilService productUtilService;

	public static final String NAME = "product_recommend";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<ProductRecommend> rec = this.productRecommendService
				.getProductRecommendsByListingId(context.getListingID());
		List<String> listingIDs = null;
		listingIDs = Lists.transform(rec, r -> r.getCrecommendlisting());
		List<ProductBadge> badges = null;
		if (null != listingIDs && listingIDs.size() > 0) {
			List<ProductBase> productBases = productEnquiryService
					.getProductBasesByListingIds(listingIDs);
			listingIDs = Lists.transform(
					Lists.newArrayList(Collections2.filter(productBases,
							i -> i.getIstatus() == 1)), j -> j.getClistingid());
			int language = foundation.getLanguage();
			int website = foundation.getSiteID();
			String currency = foundation.getCurrency();
			badges = badgeService.getProductBadgesByListingIDs(listingIDs,
					language, website, currency, null);
			// 将产品加入收藏
			productUtilService.addProductBadgeCollect(badges, listingIDs,
					language, website, currency);
		}

		return new Recommendation(badges);
	}

}

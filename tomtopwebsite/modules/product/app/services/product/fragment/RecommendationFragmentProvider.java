package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import services.product.IProductBadgeService;
import services.product.IProductFragmentProvider;
import services.product.ProductEnquiryService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.Recommendation;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dao.product.IProductRecommendEnquiryDao;
import dto.product.ProductBase;
import dto.product.ProductRecommend;

public class RecommendationFragmentProvider implements IProductFragmentProvider {

	public static final String NAME = "recommendation";

	@Inject
	IProductRecommendEnquiryDao productRecommendEnquiryDao;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		long starttime = System.currentTimeMillis();
		List<ProductRecommend> rec = this.productRecommendEnquiryDao
				.getProductRecommendsByListingId(context.getListingID());
		List<String> listingIDs = null;
		listingIDs = Lists.transform(rec, r -> r.getCrecommendlisting());
		if (null != listingIDs && listingIDs.size() > 0) {
			List<ProductBase> productBases = productEnquiryService
					.getProductBasesByListingIds(listingIDs);
			listingIDs = Lists.transform(
					Lists.newArrayList(Collections2.filter(productBases,
							i -> i.getIstatus() == 1)), j -> j.getClistingid());
		}
		Logger.debug("-->time-->RecommendationFragmentProvider-->{}",
				System.currentTimeMillis() - starttime);
		return new Recommendation(badgeService.getProductBadgesByListingIDs(
				listingIDs, context.getLang(), context.getSiteID(),
				context.getCurrency(), null));
	}
}

package services.research.fragment.provider;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.twirl.api.Html;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import dto.product.ProductBase;
import entity.activity.page.Page;
import extensions.research.IProductBadgePartProvider;
import services.product.IProductBadgeService;
import services.research.vote.IVoteFragmentProvider;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductBadgePartContext;
import valueobjects.research.vote.IVoteFragment;
import valueobjects.research.vote.ProductItemFragment;
import valueobjects.research.vote.VoteContext;

public class ProductItemFragmentProvider implements IVoteFragmentProvider {

	@Inject
	IProductBadgeService productBadgeService;

	@Inject
	services.product.IProductEnquiryService iProductEnquiryService;

	@Inject
	Set<IProductBadgePartProvider> iProductBadgePartProviderSet;

	@Override
	public IVoteFragment getFragment(VoteContext context, Page page) {
		String skustr = page.getCrecommendvalues();
		List<String> skus = Lists.newArrayList();
		if (skus != null) {
			skus.addAll(Arrays.asList(skustr.split(",")));
		}
		List<String> ListingIds = null;
		if (skus != null && skus.size() > 0) {
			List<ProductBase> productlist = iProductEnquiryService
					.getProductBaseBySkus(skus, context.getWebsiteId());
			ListingIds = Lists.transform(productlist, p -> p.getClistingid());
		}
		ProductItemFragment pif = new ProductItemFragment();
		if (ListingIds == null && ListingIds.size() == 0) {
			return pif;
		}

		List<ProductBadge> pbList = Lists.newArrayList();
		pbList = productBadgeService.getNewProductBadgesByListingIds(
				ListingIds, context.getWebsiteId(), context.getLanguageId(),
				context.getCurrency(), null);

		ListMultimap<String, Html> combined = LinkedListMultimap.create();
		ProductBadgePartContext pbpcontext = new ProductBadgePartContext(
				ListingIds, context.getWebsiteId(), context.getLanguageId(),
				context.getCurrency());
		for (IProductBadgePartProvider s : Ordering
				.natural()
				.onResultOf(
						(IProductBadgePartProvider b) -> b.getDisplayOrder())
				.sortedCopy(iProductBadgePartProviderSet)) {
			combined.putAll(Multimaps.forMap(s.getFragment(pbpcontext)));
		}
		for (String listingId : ListingIds) {
			for (ProductBadge pb : pbList) {
				if (listingId.equals(pb.getListingId())) {
					pb.setExtended(combined.get(listingId));
				}
			}
		}
		for (ProductBadge pb : pbList) {
			Logger.info(String.valueOf(pb.getExtended()));
		}
		pif.setProductBadgeList(pbList);
		return pif;
	}
}

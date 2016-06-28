package services.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.twirl.api.Html;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductBadgePartContext;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ProductUtilService {

	@Inject
	Set<IProductBadgePartProvider> badgePartProvider;

	@Inject
	Set<IProductDetailPartProvider> detailPartProvider;

	@Inject
	@Named("RatingReview")
	IProductBadgePartProvider ratingReview;

	public void addProductBadgeCollect(List<ProductBadge> badgelist,
			List<String> listingids, int lang, int site, String currency) {
		if(badgelist != null && listingids != null && badgelist.size()>0 && listingids.size()>0){
			ProductBadgePartContext partContext = new ProductBadgePartContext(
					listingids, lang, site, currency);
	
			Map<String, Html> parts = badgePartProvider.size() > 0 ? badgePartProvider
					.iterator().next().getFragment(partContext)
					: null;
			for (ProductBadge b : badgelist) {
				if (b != null && parts != null) {
					b.setExtended(Lists.newArrayList());
					b.getExtended().add(parts.get(b.getListingId()));
				}
			}
		}
	}

	public void addProductDetailCollect(ProductBadge pb,
			List<String> listingids, int lang, int site, String currency) {

		ProductBadgePartContext partContext = new ProductBadgePartContext(
				listingids, lang, site, currency);

		Html parts = detailPartProvider.size() > 0 ? detailPartProvider
				.iterator().next().getFragment(partContext) : null;
		pb.setExtended(Lists.newArrayList());
		if (pb != null && parts != null)
			pb.getExtended().add(parts);
	}

	public void addReview(List<ProductBadge> products, List<String> listingIds,
			int languageID, int site, String currency) {
		if(listingIds != null && products != null && listingIds.size()>0 && products.size()>0){
			Map<String, Html> combined = new HashMap<>();
			ProductBadgePartContext context = new ProductBadgePartContext(
					listingIds, languageID, site, currency);
			for (String key : ratingReview.getFragment(context).keySet()) {
				combined.put("review" + key,
						ratingReview.getFragment(context).get(key));
			}
			if (null != products && products.size() > 0) {
				products.forEach(c -> {
					if (c != null) {
						c.setHtmlmap(combined);
					}
				});
			}
		}
	}
}

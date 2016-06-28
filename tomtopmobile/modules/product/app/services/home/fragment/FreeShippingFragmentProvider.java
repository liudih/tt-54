package services.home.fragment;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.product.IHomePageDataEnquiry;
import services.product.homepage.AbstractHomepage;
import valueobjects.product.ProductBadge;

public class FreeShippingFragmentProvider extends AbstractHomepage implements
		ITemplateFragmentProvider {

	@Inject
	FoundationService foundation;

	@Inject
	IHomePageDataEnquiry homePageDataEnquiry;

	@Override
	public String getName() {
		return "free-shipping";
	}

	@Override
	public Html getFragment(Context context) {
		List<ProductBadge> products = super.getProductBadges(context);
		return views.html.home.free_shipping.render(products);
	}

	@Override
	public List<String> getListIds(Context context) {
		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		List<String> list = homePageDataEnquiry.getFreeshippingListingIds(
				website, language, INTEX_START_PAGE);
		return list;
	}

}

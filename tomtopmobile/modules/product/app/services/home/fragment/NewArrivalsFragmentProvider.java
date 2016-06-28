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

public class NewArrivalsFragmentProvider extends AbstractHomepage implements
		ITemplateFragmentProvider {

	@Inject
	FoundationService foundation;

	@Inject
	IHomePageDataEnquiry homePageDataEnquiry;

	@Override
	public String getName() {
		return "new-arrivals";
	}

	@Override
	public Html getFragment(Context context) {
		List<ProductBadge> products = super.getProductBadges(context);

		return views.html.home.new_arrivals.render(products);
	}

	@Override
	public List<String> getListIds(Context context) {

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		List<String> list = homePageDataEnquiry.getNewArrivalsListingIds(website, language,
				INTEX_START_PAGE);
		return list;
	}
}

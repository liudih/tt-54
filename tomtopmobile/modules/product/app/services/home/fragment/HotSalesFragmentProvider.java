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

public class HotSalesFragmentProvider extends AbstractHomepage implements
		ITemplateFragmentProvider {

	@Inject
	IHomePageDataEnquiry homePageDataEnquiry;

	@Inject
	FoundationService foundation;

	@Override
	public String getName() {
		return "hot-sales";
	}

	@Override
	public Html getFragment(Context context) {

		List<ProductBadge> products = super.getProductBadges(context);
		return views.html.home.hot_sales.render(products);
	}

	@Override
	public List<String> getListIds(Context context) {
		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		return homePageDataEnquiry.getHotSalesListingIds(website, language,
				INTEX_START_PAGE);
	}

}

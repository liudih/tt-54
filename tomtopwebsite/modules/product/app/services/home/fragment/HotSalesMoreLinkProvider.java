package services.home.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class HotSalesMoreLinkProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "hot-sales-more-link";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.home.hot_sales_more_link.render();
	}
}

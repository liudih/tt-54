package services.home.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class ClearanceSalesLinkProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "clearance-sales-more-link";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.home.clearance_sales_more_link.render();
	}

}

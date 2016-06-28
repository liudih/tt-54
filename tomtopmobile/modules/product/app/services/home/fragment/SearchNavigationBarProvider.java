package services.home.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class SearchNavigationBarProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "search-navigation-bar";
	}

	@Override
	public Html getFragment(Context context) {

		return views.html.search.search_navigation_bar.render();
	}

}

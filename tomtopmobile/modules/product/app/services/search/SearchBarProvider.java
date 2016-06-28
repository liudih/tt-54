package services.search;

import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class SearchBarProvider implements ITemplateFragmentProvider{
	
	@Override
	public String getName() {
		return "search-bar";
	}

	@Override
	public Html getFragment(Context context) {
		Logger.debug("get search bar==================================");
		return views.html.search.search_bar.render();
	}

}

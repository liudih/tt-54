package extensions.product.template;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class SearchBar implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "search-bar";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.search.search_bar_new.render();
	}

}

package extensions.product.template;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.dodocool.base.template.ITemplateFragmentProvider;

public class SearchBar implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "search-bar";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.search.search_bar.render();
	}

}

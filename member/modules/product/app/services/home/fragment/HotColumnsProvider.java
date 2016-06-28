package services.home.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class HotColumnsProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "hot_columns";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.home.hot_columns.render();
	}

}

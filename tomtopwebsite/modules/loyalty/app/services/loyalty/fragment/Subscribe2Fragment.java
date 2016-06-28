package services.loyalty.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

import com.google.inject.Inject;

public class Subscribe2Fragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "send-email-home-2";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.loyalty.subscribe.subscribe_index_2.render(context);

	}
}

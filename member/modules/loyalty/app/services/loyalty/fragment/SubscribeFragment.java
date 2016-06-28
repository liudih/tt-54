package services.loyalty.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class SubscribeFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "send-email-home";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.loyalty.subscribe.subscribe_index_1.render(context);
	}
}

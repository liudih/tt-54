package extensions.facebook.like;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class FacebookLikeProvider implements ITemplateFragmentProvider {
	
	@Override
	public String getName() {
		return "like-onfacebook";
	}

	@Override
	public Html getFragment(Context context) {
		String facebookAccount = "tomtopfans";
		return views.html.facebook.like.render(facebookAccount);
	}
}

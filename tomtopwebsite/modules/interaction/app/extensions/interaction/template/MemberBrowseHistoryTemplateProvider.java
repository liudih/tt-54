package extensions.interaction.template;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class MemberBrowseHistoryTemplateProvider implements
		ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "member-browse-history";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.interaction.fragment.member_browse_history.render();
	}

}

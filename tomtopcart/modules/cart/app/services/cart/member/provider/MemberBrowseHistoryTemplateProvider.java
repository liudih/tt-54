package services.cart.member.provider;



import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.cart.base.template.ITemplateFragmentProvider;

public class MemberBrowseHistoryTemplateProvider implements
		ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "member-browse-history";
	}

	@Override
	public Html getFragment(Context context) {
		Logger.debug("==================进入方法getFragment=====");
		return views.html.cart.member.fragment.member_browse_history.render();
	}

}

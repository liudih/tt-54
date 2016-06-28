package extensions.member;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.ILoginService;
import valueobjects.member.MemberInSession;

public class MemberNavigationBarRegion implements ITemplateFragmentProvider {

	@Inject
	FoundationService foundation;

	@Inject
	ILoginService loginService;
	
	//add by lijun
	@Inject
	private Set<IMyMessageProvider> services;
	
	@Override
	public String getName() {
		return "member-area-navigation";
	}

	@Override
	public Html getFragment(Context context) {
		if (foundation.getLoginContext().isLogin()) {
			Integer total = null;
			Iterator<IMyMessageProvider> iterator = services.iterator();
			while (iterator.hasNext()) {
				IMyMessageProvider provider = iterator.next();
				if (total == null) {
					total = provider.getUnreadTotal();
				} else {
					total = total + provider.getUnreadTotal();
				}
			}

			return views.html.member.navigation.user
					.render((MemberInSession) foundation.getLoginContext()
							.getPayload(), total);
		}
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		return views.html.member.navigation.anonymous.render(loginButtons);
	}

}

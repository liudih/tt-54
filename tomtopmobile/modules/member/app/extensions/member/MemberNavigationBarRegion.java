package extensions.member;

import java.util.List;
import java.util.Set;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import valueobjects.base.LoginContext;
import valueobjects.member.MemberInSession;

import com.google.common.collect.Ordering;
import com.google.inject.Inject;

import extensions.member.account.IMemberAccountHomeFragmentProvider;

public class MemberNavigationBarRegion implements ITemplateFragmentProvider {
	@Inject
	FoundationService foundationUtils;

	@Inject
	Set<IMemberAccountHomeFragmentProvider> homeFragments;

	@Override
	public String getName() {
		return "userCenter";
	}

	@Override
	public Html getFragment(Context context) {
		LoginContext lc = foundationUtils.getLoginContext();
		String email = "";
		if(lc.isLogin()){
			email = lc.getMemberID();
		}
		List<IMemberAccountHomeFragmentProvider> fragmentList = Ordering
				.natural()
				.onResultOf(
						(IMemberAccountHomeFragmentProvider p) -> p
								.getDisplayOrder()).sortedCopy(homeFragments);

		final String email1 = email;

		if (lc != null && lc.isLogin()) {
			return views.html.member.navigation.user.render(
					(MemberInSession) lc.getPayload(), fragmentList, email);
		}

		return views.html.member.navigation.anonymous.render(fragmentList, email);
	}

}

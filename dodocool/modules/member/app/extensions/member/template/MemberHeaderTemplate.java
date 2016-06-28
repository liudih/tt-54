package extensions.member.template;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.dodocool.base.FoundationService;
import services.dodocool.base.template.ITemplateFragmentProvider;
import valueobjects.member.MemberInSession;

import com.google.inject.Inject;

public class MemberHeaderTemplate implements ITemplateFragmentProvider {
	@Inject
	FoundationService foundationService;

	@Override
	public String getName() {
		return "member-header";
	}

	@Override
	public Html getFragment(Context context) {
		MemberInSession loginservice = (MemberInSession) foundationService
				.getLoginservice().getPayload();
		String memberId = "";
		if (null != loginservice) {
			memberId = loginservice.getUsername();
		}
		
		return views.html.member.member_header.render(memberId);
	}

}

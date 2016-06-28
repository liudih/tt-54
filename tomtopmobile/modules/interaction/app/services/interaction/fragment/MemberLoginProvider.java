package services.interaction.fragment;

import java.util.List;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.product.ICategoryEnquiryService;
import valueobjects.base.LoginContext;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.CategoryWebsiteWithName;

public class MemberLoginProvider implements ITemplateFragmentProvider {

	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Override
	public String getName() {

		return "member_login_navigation";
	}

	@Inject
	FoundationService foundation;
	
	@Override
	public Html getFragment(Context context) {
		LoginContext lc = foundation.getLoginContext();
		
		return views.html.interaction.quick.navigation.fragment.member_login_navigation.render(lc.isLogin());
	}

}

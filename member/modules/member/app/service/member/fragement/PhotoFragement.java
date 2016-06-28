package service.member.fragement;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.ILoginService;

public class PhotoFragement implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "member-photo";
	}
	
	
	
	@Inject
	ILoginService loginService;
	
	@Override
	public Html getFragment(Context context) {
		 String email=loginService.getLoginData().getEmail();
		 return views.html.member.fragment.photo.render(email);
	}

}

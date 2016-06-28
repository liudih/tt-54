package service.member.fragement;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.ILoginService;

public class LoginPopBoxFragement implements ITemplateFragmentProvider {

	@Inject
	ILoginService loginService;

	@Override
	public String getName() {
		return "login-pop";
	}

	@Override
	public Html getFragment(Context context) {
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		return views.html.member.login.login_pop.render(loginButtons);
	}

}

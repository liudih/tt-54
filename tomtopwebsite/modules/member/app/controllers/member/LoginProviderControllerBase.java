package controllers.member;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import context.ContextUtils;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.login.ILoginService;
import services.member.registration.MemberRegistrationService;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import dto.member.MemberBase;

public class LoginProviderControllerBase extends Controller {

	@Inject
	IMemberEnquiryService _enquiry;

	@Inject
	ILoginService _login;

	@Inject
	FoundationService _foundation;

	@Inject
	MemberRegistrationService registration;

	public Result loginOrRegistrationCompletion(MemberOtherIdentity otherId) {
		if (otherId != null && StringUtils.isEmpty(otherId.getEmail())) {
			flash("login_message", "Email Authorization is Required");
			return redirect(controllers.member.routes.Login.loginForm(null));
		}
		MemberBase mb = _enquiry.getMemberByOtherIdentity(otherId,
				ContextUtils.getWebContext(Context.current()));
		if (mb != null) {
			// not activated yet?
			if (mb.isBactivated()) {
				if (_login.login(otherId.getEmail(), otherId.getSource(),
						otherId.getId(),
						ContextUtils.getWebContext(Context.current()))) {
					return Login.redirectAfterLogin();
				}
			} else {
				return redirect(routes.Register.notActivatedYet(mb.getCemail()));
			}
		} else {
			// registration
			MemberRegistration reg = new MemberRegistration(otherId.getEmail(),
					null, _foundation.getCountry(), true);
			MemberRegistrationResult result = registration.register(reg,
					otherId,ContextUtils.getWebContext(Context.current()));
			if (result.isSuccess()
					&& _login.login(otherId.getEmail(), otherId.getSource(),
							otherId.getId(),
							ContextUtils.getWebContext(Context.current()))) {
				// followup questions
				return redirect(routes.Register.activateSuccessful());
			}
		}
		// error
		Logger.error("Login Failure from {}: {}", otherId.getSource(),
				otherId.getEmail());
		return Login.redirectAfterLogin();
	}
}

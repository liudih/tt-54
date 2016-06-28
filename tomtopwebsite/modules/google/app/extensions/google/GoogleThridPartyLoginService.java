package extensions.google;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.libs.Json;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.login.ILoginService;
import services.member.login.IUserInfo;
import services.member.login.OtherLoginResult;
import services.member.registration.MemberRegistrationService;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import context.WebContext;
import dto.member.LoginEnum;
import dto.member.MemberBase;
import extensions.google.login.GoogleLogin;
import extensions.member.login.IThirdPartyLoginService;

public class GoogleThridPartyLoginService implements IThirdPartyLoginService {

	@Inject
	IMemberEnquiryService _enquiry;

	@Inject
	ILoginService _login;

	@Inject
	FoundationService _foundation;

	@Inject
	MemberRegistrationService registration;

	@Inject
	GoogleLogin loginProvider;

	private OtherLoginResult loginOrRegistrationCompletion(WebContext context,
			MemberOtherIdentity otherId) {
		if (otherId != null && StringUtils.isEmpty(otherId.getEmail())) {
			// flash("login_message", "Email Authorization is Required");
			LoginEnum enu = LoginEnum.LoginNot;
			OtherLoginResult re = new OtherLoginResult(enu, null);
			return re;
		}
		MemberBase mb = _enquiry.getMemberByOtherIdentity(otherId, context);
		if (mb != null) {
			// not activated yet?
			if (mb.isBactivated()) {
				if (_login.login(otherId.getEmail(), otherId.getSource(),
						otherId.getId(), context)) {
					LoginEnum enu = LoginEnum.Success;
					OtherLoginResult re = new OtherLoginResult(enu,
							otherId.getEmail());
					return re;
				}
			} else {
				LoginEnum enu = LoginEnum.ActivitedNot;
				OtherLoginResult re = new OtherLoginResult(enu, null);
				return re;
			}
		} else {
			// registration
			MemberRegistration reg = new MemberRegistration(otherId.getEmail(),
					null, _foundation.getCountry(), true);
			MemberRegistrationResult result = registration.register(reg,
					otherId, context);
			if (result.isSuccess()
					&& _login.login(otherId.getEmail(), otherId.getSource(),
							otherId.getId(), context)) {
				// followup questions
				LoginEnum enu = LoginEnum.Success;
				OtherLoginResult re = new OtherLoginResult(enu,
						otherId.getEmail());
				return re;
			}
		}
		// error
		Logger.error("Login Failure from {}: {}", otherId.getSource(),
				otherId.getEmail());
		LoginEnum enu = LoginEnum.LoginNot;
		OtherLoginResult re = new OtherLoginResult(enu, null);
		return re;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "google";
	}

	@Override
	public OtherLoginResult login(String code, String state,
			String reredirectUri, String appId, String appSecret,
			WebContext content) {
		Logger.debug(" go in enum {} , state {} , reredirecturl {}", code,
				state, reredirectUri);
		// TODO Auto-generated method stub
		if (!StringUtils.isEmpty(code)) {
			// success, get further information from Google

			Promise<IUserInfo> userInfo = loginProvider
					.getAccessToken(code, reredirectUri, state, appId,
							appSecret)
					.flatMap(token -> loginProvider.getUserInfo(token))
					.recover(t -> {
						Logger.error("Google Login Error", t);
						return null;
					});

			return (OtherLoginResult) userInfo
					.map(u -> {
						Logger.debug("Json Result: {}", Json.toJson(u));
						if (u != null) {
							MemberOtherIdentity otherId = new MemberOtherIdentity(
									this.getName(), u.getId(), u.getEmail());
							return loginOrRegistrationCompletion(content,
									otherId);
						}
						LoginEnum enu = LoginEnum.LoginNot;
						OtherLoginResult re = new OtherLoginResult(enu, null);
						return re;
					}).get(10000);
		}
		Logger.debug("Code not returned from Google: {}, state={}", code, state);
		LoginEnum enu = LoginEnum.LoginNot;
		OtherLoginResult re = new OtherLoginResult(enu, null);
		return re;
	}

}

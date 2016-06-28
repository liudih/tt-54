package services.member;

import play.libs.F;
import services.member.login.ILoginService;
import services.member.login.LoginService;
import valueobjects.member.MemberInSession;
import extensions.InjectorInstance;

public class MemberHelper {

	public static F.Option<String> getCurrentEmail() {
		ILoginService login = InjectorInstance.getInjector().getInstance(
				LoginService.class);
		MemberInSession mis = login.getLoginData();
		if (mis == null) {
			return F.Option.<String> None();
		} else {
			return F.Option.Some(mis.getEmail());
		}
	}
}

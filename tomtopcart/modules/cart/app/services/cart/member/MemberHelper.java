package services.cart.member;

import play.libs.F;
import services.member.login.ILoginService;
import valueobjects.member.MemberInSession;

import com.google.inject.Inject;

import extensions.InjectorInstance;

public class MemberHelper {

	@Inject
	ILoginService loginService;
	public static F.Option<String> getCurrentEmail() {
		
		MemberInSession mis = InjectorInstance.getInstance(MemberHelper.class).getLoginData();
		if (mis == null) {
			return F.Option.<String> None();
		} else {
			return F.Option.Some(mis.getEmail());
		}
	}
	
	public MemberInSession getLoginData(){
		return loginService.getLoginData();
	}
}

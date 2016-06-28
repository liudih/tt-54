package services.dodocool.member;

import services.dodocool.base.FoundationService;
import services.member.login.ILoginService;
import session.ISessionService;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.member.MemberInSession;

import com.google.inject.Inject;

import dto.member.MemberBase;
import events.member.LoginEvent;

public class MemberLoginService {
	@Inject
	ISessionService sessionService;

	@Inject
	FoundationService foundationService;

	@Inject
	ILoginService loginService;

	public static final String LOGIN_SESSION_NAME = "LOGIN_CONTEXT_DODOCOOL";

	public void forceLogin(MemberBase member) {
		String sessionID = sessionService.getSessionID();
		if (sessionID == null) {
			return;
		}
		LoginContext loginCtx = foundationService.getLoginservice();
		if (member.getCaccount() == null || member.getCaccount().length() == 0) {
			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					member.getCemail().toLowerCase(), member.getCemail()
							.toLowerCase(), sessionID);
			loginCtx = PlayLoginContextFactory.newLoginContext(
					member.getCemail(), member.getIgroupid(), mis);
		} else {
			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					member.getCaccount().toLowerCase(), member.getCemail()
							.toLowerCase(), sessionID);
			loginCtx = PlayLoginContextFactory.newLoginContext(
					member.getCemail(), member.getIgroupid(), mis);
		}
		LoginEvent loginEvent = new LoginEvent(loginCtx.getLTC(),
				loginCtx.getSTC(), foundationService.getClientIP(),
				foundationService.getSiteID(),
				member.getCemail().toLowerCase(), null);

		loginService.executeLoginProcess(loginEvent);

		sessionService.set(LOGIN_SESSION_NAME, loginCtx);
	}
}

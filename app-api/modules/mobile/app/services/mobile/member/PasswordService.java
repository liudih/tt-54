package services.mobile.member;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.login.CryptoUtils;
import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;

public class PasswordService {

	@Inject
	IMemberUpdateService memberUpdateService;

	@Inject
	CryptoUtils crypto;

	@Inject
	IMemberEnquiryService menberService;

	@Inject
	LoginService loginService;

	public boolean alterPwd(String email, String pwd, Integer websiteId) {
		boolean result = memberUpdateService.SaveMemberPasswd(email,
				crypto.getHash(pwd, 2),
				ContextUtils.getWebContext(Context.current()));
		if (result) {
			loginService.loginOut();
			return true;
		}
		return false;
	}

	public boolean reset(String email, String newnwd, String oldpwd,
			WebContext context) {
		MemberBase member = menberService
				.getMemberByMemberEmail(email, context);
		if (crypto.validateHash(oldpwd, member.getCpasswd())) {
			boolean result = memberUpdateService.SaveMemberPasswd(email,
					crypto.getHash(newnwd, 2),
					ContextUtils.getWebContext(Context.current()));
			if (result) {
				loginService.loginOut();
				return true;
			}
		}
		return false;
	}
}

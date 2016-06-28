package services.dodocool.member;

import play.mvc.Http.Context;
import services.base.utils.Utils;
import services.member.IMemberEnquiryService;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;
import extensions.InjectorInstance;

public class MemberNameService {

	@Inject
	IMemberEnquiryService memberEnquiryService;

	public String getMemberAccountNameByEmail(String email) {
		WebContext context = ContextUtils.getWebContext(Context.current());
		MemberBase memberBase = memberEnquiryService.getMemberByMemberEmail(
				email, context);
		if (null != memberBase) {
			String accountName = memberBase.getCaccount();
			return null != accountName && !"".equals(accountName) ? accountName : Utils
					.getIncompleteEmail(memberBase.getCemail());
		}
		return "";
	}

	public static MemberNameService getMemberName() {
		return InjectorInstance.getInjector().getInstance(
				MemberNameService.class);
	}
}

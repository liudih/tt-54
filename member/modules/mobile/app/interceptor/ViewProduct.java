package interceptor;

import javax.inject.Inject;

import mapper.member.MemberBaseMapper;
import mapper.product.ProductBaseMapper;

import org.apache.commons.lang3.StringUtils;

import play.libs.F.Promise;
import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.mobile.MobileService;
import services.mobile.member.LoginService;

import com.google.common.eventbus.EventBus;

import dto.member.MemberBase;
import dto.product.ProductBase;
import events.mobile.ViewProductEvent;

public class ViewProduct extends Simple {

	@Inject
	EventBus eventBus;

	@Inject
	MobileService mobileService;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	LoginService loginService;

	@Inject
	MemberBaseMapper memberBaseMapper;

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		Promise<Result> result = delegate.call(ctx);
		String sku = "";
		Integer memberId = null;
		String gid = ctx.request().getQueryString("gid");
		if (StringUtils.isNotBlank(gid)) {
			ProductBase product = productBaseMapper
					.getProductBaseByListingIdAndLanguage(gid,
							mobileService.getLanguageID());
			if (product != null) {
				sku = product.getCsku();
			}
		}
		if (loginService.isLogin()) {
			String email = loginService.getLoginMemberEmail();
			MemberBase memberBase = memberBaseMapper.getUserByEmail(email,
					mobileService.getWebSiteID());
			if (memberBase != null) {
				memberId = memberBase.getIid();
			}
		}
		eventBus.post(new ViewProductEvent(gid, sku, mobileService
				.getWebSiteID(), mobileService.getUUID(), memberId,
				mobileService.getMobileContext().getIappid()));
		return result;
	}
}

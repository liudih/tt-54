package filter.tracking;

import javax.inject.Inject;

import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Result;
import service.tracking.IAffiliateService;
import services.base.FoundationService;
import services.member.login.ILoginService;
import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;

public class MarketingUserFilter implements IFilter {

	@Override
	public int priority() {
		return 10;
	}

	@Inject
	FoundationService foundation;
	@Inject
	private ILoginService loginService;
	@Inject
	IAffiliateService affiliateService;

	@Override
	public Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable {
		try {
			if (foundation.getLoginContext().isLogin()
					&& context.request().method().equals("GET")
					&& context.request().uri().contains("aid") == false) {
				String aid = affiliateService.getAidByEmail(loginService
						.getLoginEmail());
				if (aid != null) {
					String aidlink = "?";
					if ((context.request().queryString() != null && context
							.request().queryString().size() > 0)
							|| context.request().uri().contains("?")) {
						aidlink = "&";
					}
					return F.Promise.pure(play.mvc.Results.redirect(context
							.request().uri() + aidlink + "aid=" + aid));
				}
			}
		} catch (Exception ex) {
			Logger.error("auto set user aid error: ", ex);
		}
		return chain.executeNext(context);
	}

}

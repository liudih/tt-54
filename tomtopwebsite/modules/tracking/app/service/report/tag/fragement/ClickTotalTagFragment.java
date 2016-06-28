package service.report.tag.fragement;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import service.affiliate.DateRange;
import service.tracking.IAffiliateService;
import service.tracking.IVisitLogService;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.LoginService;
import dto.VisitLog;

public class ClickTotalTagFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {

		return "click-total-tag";
	}

	@Inject
	LoginService loginService;

	@Inject
	IAffiliateService affiliateService;

	@Inject
	IVisitLogService visitLogService;

	@Override
	public Html getFragment(Context context) {
		int count = 0;
		if (loginService.getLoginData() != null) {
			String email = loginService.getLoginData().getEmail();
			if (email != null) {
				String aid = affiliateService.getAidByEmail(email);
				if (null != aid) {
					DateRange range = new DateRange(-30);
					count = visitLogService.getVisitLogByDateRangeCount(aid,
							range.getBegin(), range.getEnd());
				}
			}
		}
		return views.html.report.tag.fragement.click_total_tag.render(count);
	}
}

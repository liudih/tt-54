package service.report.fragement;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class CommissionsFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {

		return "commissions-chart";
	}

	@Override
	public Html getFragment(Context context) {
		String url = controllers.affiliate.routes.Report.getCommissions().url()
				.toString();
		return views.html.report.fragement.chart.render("commissions", url);
	}

}

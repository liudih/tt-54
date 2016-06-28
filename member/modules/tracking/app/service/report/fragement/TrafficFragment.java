package service.report.fragement;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class TrafficFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {

		return "traffic-chart";
	}

	@Override
	public Html getFragment(Context context) {
		String url = controllers.affiliate.routes.Report.getTraffic().url()
				.toString();
		return views.html.report.fragement.chart.render("traffic", url);
	}

}

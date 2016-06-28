package service.report.fragement;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class SalesAmountFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {

		return "sales-amount-chart";
	}

	@Override
	public Html getFragment(Context context) {
		String url = controllers.affiliate.routes.Report.getSalesAmount().url()
				.toString();
		return views.html.report.fragement.chart.render("sales-amount", url);
	}

}

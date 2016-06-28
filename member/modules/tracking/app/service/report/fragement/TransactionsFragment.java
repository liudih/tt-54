package service.report.fragement;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class TransactionsFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {

		return "transactions-chart";
	}

	@Override
	public Html getFragment(Context context) {
		String url = controllers.affiliate.routes.Report.getTransaction().url()
				.toString();
		return views.html.report.fragement.chart.render("transactions", url);
	}

}

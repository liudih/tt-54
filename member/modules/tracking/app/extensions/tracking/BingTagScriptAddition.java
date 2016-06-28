package extensions.tracking;

import javax.inject.Inject;
import javax.inject.Singleton;

import mapper.order.DetailMapper;

import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.cart.ICartLifecycleService;
import services.product.CategoryEnquiryService;

import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;

@Singleton
public class BingTagScriptAddition implements IFilter {

	final public static String CONTEXT_VAR = "BingTag";

	@Inject
	FoundationService foundation;

	@Inject
	SystemParameterService sysparam;

	@Inject
	CategoryEnquiryService categoryEnq;

	@Inject
	ICartLifecycleService cart;

	@Inject
	DetailMapper detailMapper;

	@Inject
	CurrencyService currency;

	@Override
	public int priority() {
		return 99;
	}

	@Override
	public Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable {
		if (context.request().uri().contains("payment-confirmed")) {
			views.html.tracking.bingtag.render(context);
		}
		return chain.executeNext(context);
	}
}

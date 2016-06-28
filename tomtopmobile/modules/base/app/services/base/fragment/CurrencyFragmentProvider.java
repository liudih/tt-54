package services.base.fragment;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.ICurrencyService;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;

public class CurrencyFragmentProvider implements ITemplateFragmentProvider {

	@Inject
	ICurrencyService currencyService;

	@Inject
	FoundationService foundation;

	@Override
	public String getName() {
		return "currency-all";
	}

	@Override
	public Html getFragment(Context context) {
		// 获取当前币种
		String currentcurrency = foundation.getCurrency();

		List<dto.Currency> currencies = currencyService.getAllCurrencies();
		return views.html.base.select_currency.render(currentcurrency,
				currencies);
	}

}

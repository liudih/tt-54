package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.CurrencyService;

import com.google.common.collect.Maps;

import dto.Currency;
import dto.CurrencyRate;
import forms.ExchangeRateForm;

public class ExchangeRate extends Controller {
	@Inject
	CurrencyService currencyService;

	private final Integer ADD_CURRENCY_SUCCESS = 1;
	private final Integer ADD_CURRENCY_FAIL = 2;
	private final Integer ADD_CURRENCYCode_FAIL = 3;

	public Result index() {
		List<Currency> currencies = currencyService.getAllCurrencies();
		List<CurrencyRate> latestRates = currencyService.findLatestRate();
		List<CurrencyRate> usedRates = currencyService.findUsedRate();
		Map<String, CurrencyRate> latestMap = Maps.uniqueIndex(latestRates,
				e -> e.getCcode());
		Map<String, CurrencyRate> usedMap = Maps.uniqueIndex(usedRates,
				e -> e.getCcode());
		return ok(views.html.manager.exchange.index.render(currencies,
				latestMap, usedMap));
	}

	public Result useLatestRate(String code, int id) {
		if (null == code) {
			return badRequest();
		}
		boolean b = currencyService.useLatestRate(code, id);
		if (b) {
			return redirect(controllers.manager.routes.ExchangeRate.index());
		} else {
			return internalServerError("Update error, try again");
		}
	}

	public Result edit(int id) {
		CurrencyRate rate = currencyService.findRateById(id);
		return ok(views.html.manager.exchange.edit.render(rate));
	}

	public Result updateAndUseRate() {
		Form<ExchangeRateForm> form = Form.form(ExchangeRateForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}

		ExchangeRateForm exchangeRateForm = form.get();
		if (null == exchangeRateForm.getCode()) {
			return badRequest();
		}
		boolean f = currencyService.updateRate(exchangeRateForm.getRate(),
				exchangeRateForm.getId(), exchangeRateForm.getCode());
		boolean b = currencyService.useLatestRate(exchangeRateForm.getCode(),
				exchangeRateForm.getId());
		if (b && f) {
			return redirect(controllers.manager.routes.ExchangeRate.index());
		} else {
			return internalServerError("Update error, try again");
		}
	}

	/**
	 * 添加汇率的数据信息
	 * 
	 * @return
	 */
	public Result addCurrency() {
		Map<String, Object> map = new HashMap<String, Object>();
		Form<Currency> form = Form.form(Currency.class).bindFromRequest();
		Currency rate = form.get();
		if (rate == null) {
			return badRequest();
		}
		CurrencyRate currencyRate = new CurrencyRate();
		currencyRate.setCcode(rate.getCcode());
		currencyRate.setFexchangerate(rate.getFexchangerate());
		currencyRate.setCcreateuser(rate.getCcreateuser());
		Logger.debug("--------in------:{}",currencyRate.getCcode());
		if (currencyService.getCurrencyByCode(rate.getCcode()) == null) {
			if (currencyService.addCurrency(rate) && currencyService.addCurrencyRate(currencyRate)) {
				map.put("dataMessages", ADD_CURRENCY_SUCCESS);
			} else {
				map.put("dataMessages", ADD_CURRENCY_FAIL);
			}
		} else {
			map.put("dataMessages", ADD_CURRENCYCode_FAIL);
		}
		return ok(Json.toJson(map));
	}
}

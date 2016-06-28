package controllers.base;

import java.net.URLEncoder;
import java.util.List;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.CurrencyService;
import base.util.httpapi.ApiUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.CurrencyRate;

public class Currency extends Controller {

	@Inject
	CurrencyService currencyService;

	@Inject
	ApiUtil apiUtil;

	@BodyParser.Of(BodyParser.Json.class)
	public Result getAllCurrencies() {
		List<dto.Currency> currencies = currencyService.getAllCurrencies();
		String etag = generateETag(currencies);
		String previous = request().getHeader(IF_NONE_MATCH);
		if (etag != null && etag.equals(previous)) {
			return status(NOT_MODIFIED);
		}
		response().setHeader(CACHE_CONTROL, "max-age=604800");
		response().setHeader(ETAG, etag);
		JsonNode newJsonNode = play.libs.Json.toJson(currencies);
		return ok(newJsonNode);
	}

	public Result getCurrency() {

		Configuration config = Play.application().configuration()
				.getConfig("yahoo");
		String scur = "USD";
		String Appkey = config.getString("appkey");
		String sign = config.getString("sign");

		List<dto.Currency> currency = currencyService.getAllCurrencies();
		for (dto.Currency list : currency) {
			String tcur = list.getCcode();
			String url = "http://api.k780.com:88/?app=finance.rate&scur="
					+ scur + "&tcur=" + tcur + "&appkey=" + Appkey + "&sign="
					+ sign + "&format=json";
			String value = apiUtil.get(url);
			JsonNode jsonvalue = Json.parse(value);
			String totcur = jsonvalue.get("result").get("tcur").asText();
			Double torate = jsonvalue.get("result").get("rate").asDouble();
			CurrencyRate currencyRate = new CurrencyRate();
			currencyRate.setCcode(totcur);
			currencyRate.setFexchangerate(torate);
			currencyService.insertCurrencyRate(currencyRate);
		}
		return ok();
	}

	protected String generateETag(List<dto.Currency> currencies) {
		List<String> allName = Lists.transform(currencies, c -> c.getCcode()
				+ c.getCsymbol());
		StringBuilder sb = new StringBuilder();
		for (String s : allName) {
			sb.append(s);
		}
		return "currency-" + Integer.toHexString(sb.toString().hashCode());
	}

	public Result setCurrencyRate(String currency) {
		String tcur = currency;
		Double torate = 0d;
		try {
			String value = WS
					.url("http://download.finance.yahoo.com/d/quotes.csv")
					.setQueryParameter("e", ".csv")
					.setQueryParameter("f", "l1")
					.setQueryParameter("s", "USD" + tcur + "=x").get()
					.get(10000).getBody();
			//Logger.debug(value);
			torate = Double.valueOf(value);
			CurrencyRate currencyRate = new CurrencyRate();
			currencyRate.setCcode(tcur);
			currencyRate.setFexchangerate(torate);
			currencyService.insertCurrencyRate(currencyRate);
		} catch (Exception ex) {
			Logger.error("get currency " + tcur + " rate error", ex);
			return play.mvc.Results.internalServerError("get " + tcur
					+ " Currency error");
		}
		return ok(String.valueOf(torate));
	}
}

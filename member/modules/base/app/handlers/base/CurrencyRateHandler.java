package handlers.base;

import java.net.URLEncoder;
import java.util.List;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.Json;
import play.libs.ws.WS;
import services.base.CurrencyService;
import base.util.httpapi.ApiUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import dto.CurrencyRate;
import event.base.CurrencyRateEvent;

public class CurrencyRateHandler {

	@Inject
	CurrencyService currencyService;

	@Inject
	ApiUtil apiUtil;

	// 接口有一些国家不支持,废除这个接口
	/*
	 * @Subscribe public void onGetSuccess(CurrencyRateEvent event) {
	 * Configuration config = Play.application().configuration()
	 * .getConfig("yahoo"); String scur = "USD"; String Appkey =
	 * config.getString("appkey"); String sign = config.getString("sign");
	 * List<dto.Currency> currency = currencyService.getAllCurrencies(); for
	 * (dto.Currency list : currency) { String tcur = list.getCcode(); String
	 * url = "http://api.k780.com:88/?app=finance.rate&scur=" + scur + "&tcur="
	 * + tcur + "&appkey=" + Appkey + "&sign=" + sign + "&format=json"; String
	 * value = apiUtil.get(url); JsonNode jsonvalue = Json.parse(value); String
	 * totcur = jsonvalue.get("result").get("tcur").asText(); Double torate =
	 * jsonvalue.get("result").get("rate").asDouble(); CurrencyRate currencyRate
	 * = new CurrencyRate(); currencyRate.setCcode(totcur);
	 * currencyRate.setFexchangerate(torate);
	 * currencyService.insertCurrencyRate(currencyRate); } }
	 */

	/**
	 * s 是货币 l1是汇率 d1是日期 t1是时间
	 * http://download.finance.yahoo.com/d/quotes.csv?e=.
	 * csv&f=sl1d1t1&s=USDMXN=x
	 */
	@Subscribe
	public void onGetSuccess(CurrencyRateEvent event) {
		List<dto.Currency> currency = currencyService.getAllCurrencies();
		for (dto.Currency list : currency) {
			String tcur = list.getCcode();
			try {
				String value = WS
						.url("http://download.finance.yahoo.com/d/quotes.csv")
						.setQueryParameter("e", ".csv")
						.setQueryParameter("f", "l1")
						.setQueryParameter("s", "USD" + tcur + "=x").get()
						.get(10000).getBody();
				Double torate = Double.valueOf(value);
				CurrencyRate currencyRate = new CurrencyRate();
				currencyRate.setCcode(tcur);
				currencyRate.setFexchangerate(torate);
				currencyService.insertCurrencyRate(currencyRate);
			} catch (Exception ex) {
				Logger.error("get currency " + tcur + " rate error", ex);
			}
		}
	}
}

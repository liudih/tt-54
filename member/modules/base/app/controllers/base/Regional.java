package controllers.base;

import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.FoundationService;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dto.Country;
import dto.Currency;
import dto.Language;

public class Regional extends Controller {

	@Inject
	FoundationService foundationService;

	@Inject
	CountryService countryEnquiryService;

	@Inject
	CurrencyService currencyService;

	@Inject
	ILanguageService languageService;

	@BodyParser.Of(BodyParser.Json.class)
	public Result regionalDefaultSetting() {
		Country country = countryEnquiryService
				.getCountryByShortCountryName(foundationService.getCountry());
		Currency currency = currencyService.getCurrencyByCode(foundationService
				.getCurrency());
		Language language = languageService.getLanguage(foundationService
				.getLanguage());

		valueobjects.base.Regional regional = new valueobjects.base.Regional();
		regional.setCountryCode(country.getCshortname());
		regional.setCountryName(country.getCname());
		regional.setCurrencySymbol(currency.getCsymbol());
		regional.setCurrencyCode(currency.getCcode());
		regional.setLanguageId(language.getIid());
		regional.setLanguageName(language.getCname());

		JsonNode jsonNode = play.libs.Json.toJson(regional);

		return ok(jsonNode);
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result regionalSettings() {
		try {
			JsonNode json = request().body().asJson();
			String currencyCode = json.get("currencyCode").asText();
			String countryIsoCode = json.get("countryIsoCode").asText();
			Integer languageId = json.get("languageId").asInt();

			// save to loginContext
			foundationService.setCountry(countryIsoCode);
			foundationService.setCurrency(currencyCode);
			foundationService.setLanguage(languageId);
			return ok("success");
		} catch (Exception e) {
			Logger.error("set base info error ", e);
		}
		return ok("failure");
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result switchCurrency() {
		JsonNode json = request().body().asJson();
		String currencyCode = json.get("currencyCode").asText();
		foundationService.setCurrency(currencyCode);
		return ok("success");
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public Result switchCountry() {
		JsonNode json = request().body().asJson();
		String countryIsoCode = json.get("countryIsoCode").asText();
		foundationService.setCountry(countryIsoCode);
		return ok("success");
	}

}

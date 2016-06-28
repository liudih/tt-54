package controllers.base;

import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;
import services.base.CountryService;
import valueobjects.base.ISOCountry;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class Country extends Controller {

	@Inject
	CountryService countryService;

	public Result getAllCountries() {
		List<dto.Country> countries = countryService.getAllCountries();
		String etag = generateETag(countries);
		String previous = request().getHeader(IF_NONE_MATCH);
		if (etag != null && etag.equals(previous)) {
			return status(NOT_MODIFIED);
		}
		response().setHeader(CACHE_CONTROL, "max-age=604800");
		response().setHeader(ETAG, etag);
		JsonNode newJsonNode = play.libs.Json.toJson(Lists
				.transform(
						countries,
						c -> new ISOCountry(c.getIid(), c.getCname(), c
								.getCshortname())));
		return ok(newJsonNode);
	}

	protected String generateETag(List<dto.Country> countries) {
		List<String> allName = Lists.transform(countries, c -> c.getCname());
		StringBuilder sb = new StringBuilder();
		for (String s : allName) {
			sb.append(s);
		}
		return "country-" + Integer.toHexString(sb.toString().hashCode());
	}
}

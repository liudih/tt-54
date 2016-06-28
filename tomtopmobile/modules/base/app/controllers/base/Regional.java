package controllers.base;

import javax.inject.Inject;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;

import com.fasterxml.jackson.databind.JsonNode;

public class Regional extends Controller {

	@Inject
	FoundationService foundationService;

	@BodyParser.Of(BodyParser.Json.class)
	public Result regionalSettings() {
		JsonNode json = request().body().asJson();
		String currencyCode = json.get("currencyCode").asText();
		foundationService.setCurrency(currencyCode);
		return ok("success");
	}
}

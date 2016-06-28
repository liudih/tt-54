package controllers.manager.api;

import java.util.HashMap;
import java.util.Map;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.manager.AffiliateService;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

public class AidApiController extends Controller {
	
	@Inject
	AffiliateService affiliateService;
	
	@BodyParser.Of(BodyParser.Json.class)
	public Result saveAid(){
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		if (null == jnode || jnode.size()==0) {
			mjson.put("result", "Expecting Json data");
			return ok(Json.toJson(mjson));
		}
		mjson = affiliateService.saveAidFromMayi(jnode);
		return ok(Json.toJson(mjson));
	}
}

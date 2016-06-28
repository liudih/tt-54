package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.email.EmailSendService;

@ApiPermission
public class EmailController extends Controller {
	@Inject
	EmailSendService emailSendService;

	public Result index() {
		return ok(views.html.email.index.index.render());
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result send() {
		JsonNode json = request().body().asJson();
		String from = "";
		String toEmail = "";
		String title = "";
		String content = "";
		Map<String, Object> mjson = new HashMap<String, Object>();
		try {
			from = json.get("from").asText();
			toEmail = json.get("toEmail").asText();
			title = json.get("title").asText();
			content = json.get("content").asText();
		} catch (NullPointerException e) {
			mjson.put("result", "fail");
			return ok(Json.toJson(mjson));
		}
		JsonNode fromNameJsonNode = json.get("fromName");
		String fromName = (null == fromNameJsonNode) ? "" : fromNameJsonNode
				.asText();

		if (StringUtils.isEmpty(from) || StringUtils.isEmpty(toEmail)
				|| StringUtils.isEmpty(title) || StringUtils.isEmpty(content)) {
			mjson.put("result", "fail");
			return ok(Json.toJson(mjson));
		}
		boolean result = emailSendService.send(from, toEmail, title, content,
				fromName);
		mjson.put("result", result == true ? "success" : "fail");
		return ok(Json.toJson(mjson));
	}

}

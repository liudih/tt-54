package controllers.api;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import mapper.member.MemberBaseMapper;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.member.IMemberUpdateService;

public class Member extends Controller {

	@Inject
	IMemberUpdateService memberUpdateService;

	@BodyParser.Of(BodyParser.Json.class)
	public Result push() {
		String user = request().getHeader("user-token");
		JsonNode jnode = request().body().asJson();
		if (null == jnode) {
			return internalServerError("Expecting Json data");
		}
		String result = "";
		if (jnode.isArray()) {
			com.website.dto.member.Member[] order = Json.fromJson(jnode,
					com.website.dto.member.Member[].class);
			result = memberUpdateService.save(order);
		} else {
			com.website.dto.member.Member order = Json.fromJson(jnode,
					com.website.dto.member.Member.class);
			result = memberUpdateService
					.save(new com.website.dto.member.Member[] { order });
		}
		if (result.trim().length() == 0) {
			return ok("successfully");
		}
		return internalServerError(result);
	}
}

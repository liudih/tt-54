package controllers.api;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.loyalty.IPointsService;

public class Loyalty extends Controller {

	@Inject
	IPointsService pointsService;

	@BodyParser.Of(BodyParser.Json.class)
	public Result pushMemberPoint() {
		String user = request().getHeader("user-token");
		JsonNode jnode = request().body().asJson();
		if (null == jnode) {
			return internalServerError("Expecting Json data");
		}
		String re = "";
		if (jnode.isArray()) {
			com.website.dto.member.MemberPoint[] points = Json.fromJson(jnode,
					com.website.dto.member.MemberPoint[].class);
			re = pointsService.Save(points);
		} else {
			com.website.dto.member.MemberPoint point = Json.fromJson(jnode,
					com.website.dto.member.MemberPoint.class);
			re = pointsService
					.Save(new com.website.dto.member.MemberPoint[] { point });
		}
		if (re == null || re.trim().length() == 0) {
			return ok("successfully");
		} else {
			return internalServerError(re);
		}
	}
}

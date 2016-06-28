package controllers.interaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.interaction.InteractionProductVideoService;
import services.loyalty.PointsService;
import services.member.login.LoginService;
import valueobjects.member.MemberInSession;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;

import dto.interaction.InteractionProductMemberVideo;
import events.loyalty.ReviewEvent;
import events.loyalty.VideoEvent;

public class MemberVideo extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	LoginService loginService;

	@Inject
	InteractionProductVideoService pService;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result addVideo(String data) {
		String email = null;
		boolean result = false;
		if (foundation.getLoginContext().isLogin()) {
			email = loginService.getLoginData().getEmail();
			JsonNode jNode = play.libs.Json.parse(data);
			InteractionProductMemberVideo mVideo = new InteractionProductMemberVideo();
			Iterator<JsonNode> iterator = jNode.iterator();
			if (jNode.size() > 0) {
				while (iterator.hasNext()) {
					JsonNode next = iterator.next();
					String title = next.get("title").asText();
					String url = next.get("url").asText();
					String clistingid = next.get("clistingid").asText();
					String csku = next.get("csku").asText();
					mVideo.setCmemberemail(email);
					mVideo.setClabel(title);
					mVideo.setCvideourl(url);
					mVideo.setClistingid(clistingid);
					int site = foundation.getSiteID();
					mVideo.setIwebsiteid(site);
					mVideo.setCsku(csku);
					mVideo.setDcreatedate(new Date());
					if (pService.savaVideo(mVideo) > 0) {
						result = true;
					}
				}
			}
		}
		return ok(result + "");
	}

	public Result checkNum(String listingid) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		MemberInSession session = loginService.getLoginData();
		if (null != session) {
			boolean flag = pService
					.checkVideoNum(session.getEmail(), listingid);
			if (flag) {
				mjson.put("result", "success");
			}
		}
		return ok(Json.toJson(mjson));
	}
}

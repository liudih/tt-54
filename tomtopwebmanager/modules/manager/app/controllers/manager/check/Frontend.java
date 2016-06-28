package controllers.manager.check;

import java.util.Map;

import javax.inject.Inject;

import context.ContextUtils;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import services.member.MemberBaseService;

public class Frontend extends Controller {

	@Inject
	MemberBaseService memberBaseServer;

	/**
	 * 验证前台用户的存在性
	 * 
	 * @param email
	 * @return
	 */
	public Result emailExist() {
		RequestBody body = request().body();
		if (body != null) {
			Map<String, String[]> map = body.asFormUrlEncoded();
			if (map != null) {
				String[] emails = map.get("cemail");
				if (emails != null && emails.length > 0) {
					String email = emails[0];
					if (memberBaseServer.getMember(email,
							ContextUtils.getWebContext(Context.current())) != null) {
						return ok("true");
					}
				}
			}
		}
		return ok("false");
	}
}
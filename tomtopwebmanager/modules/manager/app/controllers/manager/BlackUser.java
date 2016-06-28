package controllers.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import play.libs.Json;
import javax.inject.Inject;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.member.IMemberBlackUserService;

/**
 * 会员黑名单核心控制器
 * 
 * @author guozy
 *
 */
public class BlackUser extends Controller {

	@Inject
	private IMemberBlackUserService blackUserService;

	@Inject
	private FoundationService foundation;

	/**
	 * 传达错误提示
	 */
	private static Integer BLACK_USER_STATUS = 0;
	private static Integer OPERATE_SUCCESS = 1;
	private static Integer REMOVE_BLACK_USER_SCCESS = 2;
	private static Integer REMOVE_BLACK_USER_FAIL = 3;

	/**
	 * 添加黑名单用户
	 * 
	 * @return
	 */
	public Result addBlackUser() {
		Map<String, Object> map = new HashMap<String, Object>();
		Form<dto.member.BlackUser> blackUserRequestForm = Form.form(
				dto.member.BlackUser.class).bindFromRequest();
		dto.member.BlackUser blackUsers = blackUserRequestForm.get();

		if (blackUserRequestForm.hasErrors()) {
			return badRequest();
		}
		String olderEmail = blackUsers.getCemail();
		dto.member.BlackUser newEmails = blackUserService
				.getBlackUserEamil(olderEmail);
		blackUsers.setDcreatedate(new Date());
		blackUsers.setIwebsiteid(foundation.getSiteID());
		blackUsers.setIstatus(BLACK_USER_STATUS);

		if (newEmails == null || newEmails.equals("")) {
			blackUserService.insertBlackUser(blackUsers);
		} else {
			blackUserService.updateBlackUser(blackUsers);
		}
		map.put("dataMessages", OPERATE_SUCCESS);
		return ok(Json.toJson(map));
	};

	/**
	 * 移除黑客户数据信息
	 * 
	 * @return
	 */
	public Result removeBlackUser() {
		Map<String, Object> map = new HashMap<String, Object>();
		Form<dto.member.BlackUser> blackUserRequestForm = Form.form(
				dto.member.BlackUser.class).bindFromRequest();
		dto.member.BlackUser blackUsers = blackUserRequestForm.get();
		if (null == blackUsers) {
			return badRequest();
		}
		String cemail = blackUsers.getCemail();
		if (blackUserService.removeBlackUser(cemail)) {
			map.put("dataMessages", REMOVE_BLACK_USER_SCCESS);
			return ok(Json.toJson(map));
		}
		map.put("dataMessages", REMOVE_BLACK_USER_FAIL);
		return ok(Json.toJson(map));
	};

}

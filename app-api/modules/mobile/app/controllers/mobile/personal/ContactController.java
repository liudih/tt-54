package controllers.mobile.personal;

import java.util.HashMap;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.mobile.MobileService;
import services.mobile.personal.ContactService;
import utils.MsgUtils;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;

public class ContactController extends TokenController {

	@Inject
	ContactService contactService;

	@Inject
	MobileService serveice;

	/**
	 * 添加联系我们
	 * 
	 * @return
	 */
	public Result saveContact(String title, String content, String email) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			objMap = contactService.checkContact(title, content, email);
			Integer res = (Integer) objMap.get("re");
			if (res != BaseResultType.SUCCESS) {
				ok(Json.toJson(objMap));
			}
			boolean flag = contactService.saveContact(title, content, email);
			if (flag) {
				objMap.put("re", BaseResultType.SUCCESS);
				objMap.put("msg", "");
			} else {
				objMap.put("re", BaseResultType.FAILURE);
				objMap.put("msg", MsgUtils.msg(BaseResultType.OPERATE_FAIL));
			}
			return ok(Json.toJson(objMap));
		} catch (Exception e) {
			Logger.error("ContactController.saveContact Exception",
					e.fillInStackTrace());
			e.printStackTrace();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(objMap));
		}
	}
}

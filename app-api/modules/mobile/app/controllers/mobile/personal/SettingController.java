package controllers.mobile.personal;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.mobile.MobileService;
import services.mobile.personal.SettingService;
import utils.MsgUtils;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.MobileContext;
import controllers.mobile.TokenController;

public class SettingController extends TokenController {

	@Inject
	SettingService settingService;

	@Inject
	MobileService serveice;
	/**
	 * 添加或修改设置
	 * 
	 * @return
	 */
	public Result updateSetting(Integer type, String val) {
		BaseJson result = new BaseJson();
		try {
			if (!settingService.checkSetting(type, val)) {
				result.setRe(BaseResultType.ERROR);
				result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
				return ok(Json.toJson(result));
			}
			boolean flag = settingService.updateSetting(type, val);
			if (flag) {
				MobileContext mobileContext = serveice.getMobileContext();
				int typeId = Integer.valueOf(val);
				switch (type) {
				case 1:
					mobileContext.setIcountryid(typeId);
					break;
				case 2:
					mobileContext.setIlanguageid(typeId);
					break;
				case 3:
					mobileContext.setIcurrencyid(typeId);
					break;
				}
				serveice.setMobileContext(mobileContext);
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				return ok(Json.toJson(result));
			}
			result.setRe(BaseResultType.ERROR);
			result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
			return ok(Json.toJson(result));
		} catch (Exception e) {
			Logger.error("SettingController.updateSetting Exception", e);
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
	}

}

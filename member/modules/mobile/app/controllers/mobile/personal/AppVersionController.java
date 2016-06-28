package controllers.mobile.personal;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Result;
import services.mobile.personal.AppVersionService;
import valuesobject.mobile.BaseInfoJson;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;
import dto.mobile.AppVersionInfo;
import dto.mobile.VersionInfo;

public class AppVersionController extends TokenController {

	@Inject
	AppVersionService appVersionService;

	/**
	 * 检测app版本
	 * 
	 * @return
	 */
	public Result getMaxAppVersion(String vs) {
		AppVersionInfo currVs = appVersionService.getAppVersionByVs(vs);
		AppVersionInfo maxVs = appVersionService.getMaxAppVersion();

		if (currVs != null && maxVs != null) {
			if (maxVs.getVid().intValue() > currVs.getVid().intValue()) {
				VersionInfo versionInfo = new VersionInfo();
				versionInfo.setDescr(maxVs.getDescr());
				versionInfo.setDurl(maxVs.getDurl());
				versionInfo.setVs(maxVs.getVs());
				BaseInfoJson<VersionInfo> result = new BaseInfoJson<>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setInfo(versionInfo);
				return ok(Json.toJson(result));
			}
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.OPERATE_FAIL);
		return ok(Json.toJson(result));
	}
}

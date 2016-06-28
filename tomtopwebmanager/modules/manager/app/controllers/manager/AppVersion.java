package controllers.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.manager.AppVersionService;
import session.ISessionService;
import dto.mobile.AppVersionDto;
import forms.AppVersionForm;

@controllers.AdminRole(menuName = "VersionMgr")
public class AppVersion extends Controller {

	@Inject
	ISessionService sessionService;

	@Inject
	AppVersionService appVersionService;

	public Result list() {
		List<AppVersionDto> versionList = appVersionService.getAllAppVersion();
		return ok(views.html.manager.appversion.version_manager
				.render(versionList));
	}

	public Result addAppVersion() {
		Logger.debug("添加版本");
		Form<AppVersionForm> form = Form.form(AppVersionForm.class)
				.bindFromRequest();

		AppVersionDto appVersionDto = new AppVersionDto();
		AppVersionForm appVersionForm = form.get();
		BeanUtils.copyProperties(appVersionForm, appVersionDto);

		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		appVersionDto.setCreateuser(user.getCcreateuser());
		appVersionDto.setCreatedate(new Date());
		AppVersionDto param = appVersionService.addAppVersion(appVersionDto);
		return ok(Json.toJson(param));
	}

	public Result updateAppVersion() {
		Logger.debug("修改版本");
		Form<AppVersionForm> form = Form.form(AppVersionForm.class)
				.bindFromRequest();

		AppVersionDto appVersionDto = new AppVersionDto();
		AppVersionForm appVersionForm = form.get();
		BeanUtils.copyProperties(appVersionForm, appVersionDto);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (appVersionService.updateAppVersion(appVersionDto)) {
			resultMap.put("errorCode", 1);
			return ok(Json.toJson(resultMap));
		}
		Logger.debug("修改版本失败");
		resultMap.put("errorCode", 0);
		return ok(Json.toJson(resultMap));
	}

	public Result deleteAppVersion(Integer iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (appVersionService.deleteAppVersion(iid)) {
			resultMap.put("errorCode", 1);
			resultMap.put("msg", "版本删除成功");
		} else {
			resultMap.put("errorCode", 0);
			resultMap.put("msg", "删除失败");
		}
		return ok(Json.toJson(resultMap));
	}

}

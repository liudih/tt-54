package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.image.FilePathEnquiryService;
import services.image.UploadFilePathUpdateService;
import session.ISessionService;
import controllers.InterceptActon;
import entity.manager.AdminUser;

@With(InterceptActon.class)
public class UploadFilePath extends Controller {
	@Inject
	FilePathEnquiryService filePathEnquiryService;

	@Inject
	UploadFilePathUpdateService uploadFilePathUpdateService;

	@Inject
	ISessionService sessionService;

	public Result uploadFilePathManager() {
		List<dto.image.UploadFilePath> allFilePath = filePathEnquiryService
				.getAllFilePath();
		return ok(views.html.manager.uploadfilepath.uploadfilepath_manager
				.render(allFilePath));
	}

	public Result saveUploadFilePath() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		dto.image.UploadFilePath form = Form
				.form(dto.image.UploadFilePath.class).bindFromRequest()
				.get();
		String path = form.getCpath();
		form.setCpath(path);
		dto.image.UploadFilePath uFilePath = filePathEnquiryService
				.getUploadFilePathByPath(form.getCpath());
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		if (uFilePath != null) {
			result.put("fail", false);

			return ok(Json.toJson(result));
		}
		form.setCcreateuser(user.getCcreateuser());
		boolean insertUploadFilePath = uploadFilePathUpdateService
				.insertUploadFilePath(form);
		if (insertUploadFilePath) {
			result.put("result", true);
		} else {
			result.put("result", false);
		}

		return ok(Json.toJson(result));
	}

	public Result deleteUploadFilePath(Integer iid) {
		boolean delete = uploadFilePathUpdateService.deleteUploadFilePath(iid);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", delete);

		return ok(Json.toJson(result));
	}
}

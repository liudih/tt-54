package controllers.manager;


import java.io.IOException;
import javax.inject.Inject;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import services.ckeditor.CkeditorService;

public class Ckeditor extends Controller {
	@Inject
	CkeditorService ckeditorService;

	public Result upload() throws IOException {
		String cKEditorFuncNum = request().getQueryString("CKEditorFuncNum");
		MultipartFormData body = request().body().asMultipartFormData();
		String fileType = request().getHeader("Content-Type");
		String imageurl = ckeditorService.upload(fileType, body);
		if (null == imageurl) {
			return ok("文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）");
		}
		response().setContentType("text/html;charset=UTF-8");

		return ok("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction("+ cKEditorFuncNum +", '/img/"
				+ imageurl + "');</script>");
	}

}

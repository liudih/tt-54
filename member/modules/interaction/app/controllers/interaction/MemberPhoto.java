package controllers.interaction;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import authenticators.member.MemberLoginAuthenticator;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Security.Authenticated;
import services.interaction.MemberPhotoUploadService;

public class MemberPhoto extends Controller{
	
	final static int NOT_ERROR = 0;

	final static int SERVER_ERROR = 1;

	final static int CONTENT_TYPE_ERROR = 2;

	final static int SIZE_ERROR = 3;

	final static int ACCEPT_SIZE = 2;
	
	@Inject
	MemberPhotoUploadService photouploadService;
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result pushWithSession() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		MultipartFormData body = request().body().asMultipartFormData();

		FilePart part = body.getFile("file");
		String contentType = part.getContentType();

		if (!checkContentType(contentType)) {
			resultMap.put("errorCode", CONTENT_TYPE_ERROR);
			return ok(Json.toJson(resultMap));
		}

		File file = part.getFile();
		if (file != null) {
			if (!checkSize(file.length())) {
				resultMap.put("errorCode", SIZE_ERROR);
				return ok(Json.toJson(resultMap));
			}
			photouploadService.pushWithSession(file, contentType);
			resultMap.put("errorCode", NOT_ERROR);
			return ok(Json.toJson(resultMap));
		}

		resultMap.put("errorCode", SERVER_ERROR);
		return ok(Json.toJson(resultMap));
	}
	
	private boolean checkSize(long size) {
		int _size = new Integer((int) (size / 1024));
		return _size <= ACCEPT_SIZE ? true : false;
	}
	
	private boolean checkContentType(String contentType) {
		Pattern pattern = Pattern.compile("^(image)/(gif|png|jpe?g)$");
		Matcher matcher = pattern.matcher(contentType);
		return matcher.matches();
	}
}

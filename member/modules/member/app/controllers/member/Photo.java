package controllers.member;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import context.ContextUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.member.IMemberPhotoService;
import authenticators.member.MemberLoginAuthenticator;
import dto.member.MemberPhoto;

public class Photo extends Controller {

	final static int NOT_ERROR = 0;

	final static int SERVER_ERROR = 1;

	final static int CONTENT_TYPE_ERROR = 2;

	final static int SIZE_ERROR = 3;

	final static int ACCEPT_SIZE = 50;

	@Inject
	IMemberPhotoService memberPhotoService;

	@Inject
	FoundationService foundationService;

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
			memberPhotoService.pushWithSession(file, contentType);
			resultMap.put("errorCode", NOT_ERROR);
			return ok(Json.toJson(resultMap));
		}

		resultMap.put("errorCode", SERVER_ERROR);
		return ok(Json.toJson(resultMap));

	}

	public Result get(String email) {
		int websiteId = foundationService.getSiteID();
		MemberPhoto photo = memberPhotoService.getPhoto(email,
				ContextUtils.getWebContext(Context.current()));
		if (photo != null && photo.getBfile() != null) {
			String etag = photo.getCmd5();
			String match = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(match)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(photo.getBfile()).as(photo.getCcontenttype());
		}
		return redirect(routes.Assets.at("/images/HeadPic.jpg"));
	}

	public Result push() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean target = memberPhotoService.updatePhoto();
		if (target) {
			resultMap.put("errorCode", NOT_ERROR);
		} else {
			resultMap.put("errorCode", SERVER_ERROR);
		}
		return ok(Json.toJson(resultMap));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result getWithSession() {
		return ok(memberPhotoService.getWithSession()).as(
				memberPhotoService.getContentTypeWithSession());
	}

	private boolean checkSize(long size) {
		int _size = new Integer((int) (size / 1024));
		return _size <= ACCEPT_SIZE ? true : false;
	}

	private boolean checkContentType(String contentType) {
		Pattern pattern = Pattern.compile("^(image)/(bmp|gif|png|jpe?g)$");
		Matcher matcher = pattern.matcher(contentType);
		return matcher.matches();

	}

}

package controllers.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Hex;

import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.dodocool.base.FoundationService;
import services.member.IMemberPhotoService;
import authenticators.member.MemberLoginAuthenticator;
import dto.member.MemberPhoto;
import forms.dodocool.member.MemberBaseForm;

public class Photo extends Controller {

	@Inject
	IMemberPhotoService memberPhotoService;

	@Inject
	FoundationService foundationService;

	final int UPLOADS_FAIL = 1;
	final int CONTENT_TYPE_ERROR = 2;
	final int CHECK_LENGTH_ERROR = 3;
	final int ACCEPT_LENGTH = 30;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result uploads() throws Exception {
		Form<MemberBaseForm> memberUpdateForm = Form.form(MemberBaseForm.class)
				.bindFromRequest();
		MemberBaseForm form = memberUpdateForm.get();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("file_name");
		String cemail = form.getCemail();
		String errorMessage=null;
		try {
			if (filePart != null) {
				String contentType = filePart.getContentType();
				Pattern pattern = Pattern.compile("^(image)/(bmp|gif|png|jpe?g)$");
				Matcher matcher = pattern.matcher(contentType);
				boolean checkContentType = matcher.matches();
				File file = filePart.getFile();
				if (!checkContentType) {
					resultMap.put("errorCode", CONTENT_TYPE_ERROR);
					errorMessage = Messages.get("image.format.error");
					resultMap.put("errorMessage", errorMessage);
					return ok(Json.toJson(resultMap));
				}
				if (file != null) {
					double file_length=Double.valueOf(file.length())/1024;
					if (file.length() > (ACCEPT_LENGTH*1024)) {
						resultMap.put("errorCode", CHECK_LENGTH_ERROR);
						errorMessage = MessageFormat.format(Messages.get("photo.size.error"), file_length , ACCEPT_LENGTH);
						resultMap.put("errorMessage", errorMessage);
						return ok(Json.toJson(resultMap));
					}
					InputStream is = new FileInputStream(file);
					byte[] buff = org.apache.commons.io.IOUtils.toByteArray(is);

					MemberPhoto memberPhoto  = new MemberPhoto();
					
					int siteId = foundationService.getSiteID();
					memberPhoto.setIwebsiteid(siteId);
					memberPhoto.setBfile(buff);
					memberPhoto.setCemail(cemail);
					memberPhoto.setCcontenttype(contentType);
					memberPhoto.setCmd5(Hex.encodeHexString(MessageDigest
							.getInstance("MD5").digest(buff)));
					boolean result = memberPhotoService
							.updateMemberPhoto(memberPhoto);
				}
			}
			return ok(Json.toJson(resultMap));
		} catch (Exception e) {
			// TODO: handle exception UPLOADS_FAIL
			resultMap.put("errorCode", UPLOADS_FAIL);
			errorMessage = Messages.get("upload.error");
			resultMap.put("errorMessage", errorMessage);
			return ok(Json.toJson(resultMap));
		}
	}
}

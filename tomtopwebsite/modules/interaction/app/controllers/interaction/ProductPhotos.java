package controllers.interaction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import dto.interaction.InteractionProductMemberPhotos;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.interaciton.review.ProductReviewsService;
import services.interaction.ProductPhotosService;
import services.member.login.LoginService;
import valueobjects.member.MemberInSession;
import authenticators.member.MemberLoginAuthenticator;
import forms.interaction.ProductPhotosForm;

public class ProductPhotos extends Controller {

	final int notError = 0;

	final int typeError = -1;

	final int serverError = -2;

	final int uploadError = -3;

	final int maxNumEerror = -4;

	final int maxUploadNum = 5;

	@Inject
	ProductPhotosService photosService;

	@Inject
	LoginService loginService;

	@Inject
	ProductReviewsService productReviewsService;
	@Inject
	FoundationService foundationService;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result pushPreveiwImageWithSession() {		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		MultipartFormData body = request().body().asMultipartFormData();

		Form<ProductPhotosForm> form = Form.form(ProductPhotosForm.class)
				.bindFromRequest();

		if (form.hasErrors()) {
			resultMap.put("required", form.errorsAsJson());
			resultMap.put("errorCode", uploadError);
			return ok(Json.toJson(resultMap));
		}
		ProductPhotosForm commnetForm = form.get();
		Integer index = commnetForm.getIndex();
		int count = photosService.getPreveiwImageCount();

		if (count >= maxUploadNum) {
			resultMap.put("errorCode", maxNumEerror);
			return ok(Json.toJson(resultMap));
		}

		List<FilePart> parts = body.getFiles();
		for (FilePart part : parts) {
			String contentType = part.getContentType();

			if (!checkContentType(contentType)) {
				resultMap.put("errorCode", typeError);
				return ok(Json.toJson(resultMap));
			}

			File file = part.getFile();
			if (file != null) {
				photosService.pushPreveiwImageWithSession(file,
						Integer.valueOf(index), contentType);
				resultMap.put("index", index);
				resultMap.put("errorCode", notError);
				//返回长、宽
				BufferedImage sourceImg;
				try {
					sourceImg = ImageIO.read(new FileInputStream(file));
					resultMap.put("width", sourceImg.getWidth());
					resultMap.put("height", sourceImg.getHeight());
				} catch (FileNotFoundException e) {
					Logger.error("File not found" + e.getMessage(), e);
				} catch (IOException e) {
					Logger.error("File exception" + e.getMessage(), e);
				}
				return ok(Json.toJson(resultMap));
			}

		}
		resultMap.put("errorCode", serverError);
		return ok(Json.toJson(resultMap));
	}

	public Result getPreveiwImageWithSession(int index) {
		return ok(photosService.getPreveiwImageWithSession(index)).as(
				photosService.getContentTypeWithSession(index));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result delPreveiwImageWithSession(int index) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		photosService.delPreveiwImageWithSession(index);
		resultMap.put("index", index);
		resultMap.put("errorCode", notError);
		return ok(Json.toJson(resultMap));
	}

	private boolean checkContentType(String contentType) {
		Pattern pattern = Pattern.compile("^(image)/(gif|png|jpeg)$");
		Matcher matcher = pattern.matcher(contentType);
		return matcher.matches();
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result addComment() {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<ProductPhotosForm> form = Form.form(ProductPhotosForm.class)
				.bindFromRequest();

		if (form.hasErrors()) {
			resultMap.put("required", form.errorsAsJson());
			resultMap.put("errorCode", uploadError);
			return ok(Json.toJson(resultMap));
		}
		ProductPhotosForm commentForm = form.get();
		String comment = commentForm.getComment();
		String previewIndexs = commentForm.getIndexs();
		String listingid = commentForm.getClistingid();
		String csku = commentForm.getCsku();
		int site = foundationService.getSiteID();
		String email = loginService.getLoginData().getEmail();
		boolean result = photosService.addphotos(comment, previewIndexs,
				listingid,csku,site,null,email);
		if (result) {
			resultMap.put("errorCode", notError);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", serverError);
		return ok(Json.toJson(resultMap));

	}

	public Result at(int iid) {
		InteractionProductMemberPhotos p = photosService.getPhotoById(iid);
		if (p != null && p.getBfile()!=null) {
			return ok(p.getBfile()).as(p.getCcontenttype());
		}
		return badRequest();
	}

	// 检验是否可上传图片
	@Authenticated(MemberLoginAuthenticator.class)
	public Result checkIsUpload(String listingId) {
		MemberInSession session = loginService.getLoginData();
		boolean result = productReviewsService.checkUploadPhoto(
				session.getEmail(), listingId);
		return ok(result + "");
	}
	
	//检查上传数量
	@Authenticated(MemberLoginAuthenticator.class)
	public Result checkNum(String listingid,Integer updatenum){
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		MemberInSession session = loginService.getLoginData();
		boolean result = photosService.checkPhotoNum(
				session.getEmail(), listingid,updatenum);
		if(result){
			mjson.put("result", "success");
		}		
		return ok(Json.toJson(mjson));
	}

}

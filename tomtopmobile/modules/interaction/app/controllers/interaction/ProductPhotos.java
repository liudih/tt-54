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

import controllers.MemberLoginValidate;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.base.FoundationService;
import services.interaction.IProductPhotosService;
import services.interaction.review.IProductReviewsService;
import services.member.login.ILoginService;
import valueobjects.base.LoginContext;
import valueobjects.member.MemberInSession;
import dto.interaction.InteractionProductMemberPhotos;
import forms.interaction.ProductPhotosForm;

public class ProductPhotos extends Controller {
	final int notError = 0;

	final int typeError = -1;

	final int serverError = -2;

	final int uploadError = -3;

	final int maxNumEerror = -4;

	final int maxUploadNum = 5;
	@Inject
	IProductPhotosService photosService;

	@Inject
	ILoginService loginService;

	@Inject
	IProductReviewsService productReviewsService;
	@Inject
	FoundationService foundationService;

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

	private boolean checkContentType(String contentType) {
		Pattern pattern = Pattern.compile("^(image)/(gif|png|jpeg)$");
		Matcher matcher = pattern.matcher(contentType);
		return matcher.matches();
	}

	@MemberLoginValidate
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
		LoginContext lc = foundationService.getLoginContext();
		String email = lc.isLogin() ? lc.getMemberID() : "";
		boolean result = photosService.addphotos(comment, previewIndexs,
				listingid, csku, site, null, email);
		if (result) {
			resultMap.put("errorCode", notError);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", serverError);
		return ok(Json.toJson(resultMap));

	}

	// 检验是否可上传图片
	public Result checkIsUpload(String listingId) {
		LoginContext lc = foundationService.getLoginContext();
		String email = lc.isLogin() ? lc.getMemberID() : "";
		boolean result = productReviewsService.checkUploadPhoto(
				email, listingId);
		return ok(result + "");
	}
	
	//检查上传数量
	public Result checkNum(String listingid,Integer updatenum){
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		
		LoginContext lc = foundationService.getLoginContext();
		String email = lc.isLogin() ? lc.getMemberID() : "";
		boolean result = photosService.checkPhotoNum(
				email, listingid,updatenum);
		if(result){
			mjson.put("result", "success");
		}		
		return ok(Json.toJson(mjson));
	}

}

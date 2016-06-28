package controllers.mobile.product;

import forms.mobile.AddReviewForm;
import interceptor.auth.LoginAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.product.ProductReviewService;
import utils.CommonDefn;
import utils.MsgUtils;
import utils.ValidataUtils;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;

import com.google.common.collect.Maps;

import controllers.mobile.TokenController;

@With(LoginAuth.class)
public class ProductReviewController extends TokenController {

	@Inject
	LoginService loginService;
	@Inject
	MobileService mobileService;
	@Inject
	ProductReviewService reviewService;

	public Result addReview() {
		Map<String, Object> resultMap = Maps.newHashMap();
		String email = loginService
				.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
		Integer siteId = mobileService.getWebSiteID();
		Integer langId = mobileService.getLanguageID();

		Form<AddReviewForm> form = Form.form(AddReviewForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			resultMap.put("re", BaseResultType.REVIEW_ADD_FORM_GET_ERROR_CODE);
			resultMap.put("msg",
					MsgUtils.msg(BaseResultType.REVIEW_ADD_FORM_GET_ERROR_MSG));
			return ok(Json.toJson(resultMap));
		}
		AddReviewForm reviewForm = form.get();
		MultipartFormData body = request().body().asMultipartFormData();
		List<FilePart> parts = new ArrayList<FilePart>();
		if (body != null) {
			parts = body.getFiles();
			if (parts != null && parts.size() > 0) {
				FileChannel fc = null;
				FileInputStream fis = null;
				File file = parts.get(0).getFile();
				String contenttype = parts.get(0).getContentType();
				if (file.exists() == false) {
					return ok(Json
							.toJson(new BaseJson(
									BaseResultType.UPLOAD_USER_IMG_NOT_FILE_ERROR_CODE,
									MsgUtils.msg(BaseResultType.UPLOAD_USER_IMG_NOT_FILE_ERROR_MSG))));
				}
				try {
					fis = new FileInputStream(file);
					fc = fis.getChannel();
					if (ValidataUtils.checkContentType(contenttype) == false) {
						return ok(Json
								.toJson(new BaseJson(
										BaseResultType.UPLOAD_USER_IMG_CONTENT_TYPE_ERROR_CODE,
										MsgUtils.msg(BaseResultType.UPLOAD_USER_IMG_CONTENT_TYPE_ERROR_MSG))));
					}
					if (ValidataUtils.checkSize(fc.size(), 1000) == false) {
						return ok(Json
								.toJson(new BaseJson(
										BaseResultType.UPLOAD_REVIEW_IMG_FILE_SIZE_OVER_MAX_ERROR_CODE,
										MsgUtils.msg(BaseResultType.UPLOAD_REVIEW_IMG_FILE_SIZE_OVER_MAX_ERROR_MSG))));
					}
				} catch (Exception e) {
					resultMap.put("re", BaseResultType.EXCEPTION);
					resultMap.put("msg",
							MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
					Logger.error("ProductReviewController addReview Exception",
							e);
				} finally {
					try {
						if (null != fc) {
							fc.close();
						}
						if (null != fis) {
							fis.close();
						}
					} catch (IOException e) {
						Logger.error(
								"ProductReviewController addReview IOException",
								e);
						e.printStackTrace();
					}
				}
			}
		}
		resultMap = reviewService.addReviewImgVideo(reviewForm, email, siteId,
				langId, parts);
		Promise.promise(() -> reviewService.sendEmailReviewSubmit(email));
		return ok(Json.toJson(resultMap));
	}
}

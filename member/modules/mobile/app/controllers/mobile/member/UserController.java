package controllers.mobile.member;

import interceptor.auth.LoginAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.member.UserService;
import utils.CommonDefn;
import utils.DateUtils;
import utils.Page;
import utils.ValidataUtils;
import valuesobject.mobile.BaseInfoJson;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BasePageJson;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;
import dto.mobile.CouponsInfo;
import dto.mobile.UserInfo;
import forms.mobile.UserInfoForm;

@With(LoginAuth.class)
public class UserController extends TokenController {

	@Inject
	UserService userSvc;
	@Inject
	LoginService loginService;
	@Inject
	MobileService mobileService;

	/**
	 * 个人中心首页
	 * 
	 */
	public Result userMsg() {
		BaseInfoJson<UserInfo> result = new BaseInfoJson<UserInfo>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Integer siteId = mobileService.getWebSiteID();
			UserInfo userInfo = userSvc.getUserInfo(email, siteId);

			if (null == userInfo) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						BaseResultType.NODATA)));
			}

			result.setInfo(userInfo);
			result.setRe(BaseResultType.SUCCESS);
			result.setMsg("");

		} catch (Exception e) {
			HashMap<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController userMsg() Exception", e);
			e.printStackTrace();
			return ok(Json.toJson(objMap));
		}

		return ok(Json.toJson(result));

	}

	/**
	 * 优惠劵信息
	 * 
	 */
	public Result coupon(int page, int pageSize, int type) {
		BasePageJson<CouponsInfo> result = new BasePageJson<CouponsInfo>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Page<CouponsInfo> cPage = null;

			if (type == 0) {
				cPage = userSvc.getUnusedCoupons(page, pageSize, email);
			}
			if (type == 1) {
				cPage = userSvc.getUsedCoupons(page, pageSize, email);
			}
			if (null == cPage) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						BaseResultType.NODATA)));
			}

			result.setList(cPage.getList());
			result.setTotal(cPage.getTotal());
			result.setP(cPage.getP());
			result.setSize(cPage.getSize());
			result.setRe(BaseResultType.SUCCESS);
			result.setMsg("");
		} catch (Exception e) {
			HashMap<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController coupon() Exception", e);
			e.printStackTrace();
			return ok(Json.toJson(objMap));
		}
		return ok(Json.toJson(result));

	}

	/**
	 * 个人资料信息
	 * 
	 */
	public Result peronInfo() {
		Map<String, Object> pMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Integer siteId = mobileService.getWebSiteID();
			pMap = userSvc.getPeronInfo(email, siteId);
			if (null == pMap) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						BaseResultType.NODATA)));
			}
			pMap.put("re", BaseResultType.SUCCESS);
			pMap.put("msg", "");
		} catch (Exception e) {
			HashMap<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController peronInfo() Exception", e);
			e.printStackTrace();
			return ok(Json.toJson(objMap));
		}
		return ok(Json.toJson(pMap));
	}

	/**
	 * 更新个人资料
	 * 
	 * UserInfoForm
	 * 
	 * @return
	 */
	public Result updatePeronInfo() {
		BaseJson result = new BaseJson();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Integer siteId = mobileService.getWebSiteID();
			Form<UserInfoForm> form = Form.form(UserInfoForm.class)
					.bindFromRequest();
			UserInfoForm userform = form.get();

			result = validateUserForm(userform);
			if (result.getRe() < 0) {
				return ok(Json.toJson(result));
			}
			boolean b = false;
			b = userSvc.update(email, siteId, userform.getNickname(),
					userform.getFname(), userform.getLname(),
					userform.getGender(),
					DateUtils.strForDate(userform.getBirth()),
					userform.getCountry(), userform.getAbout());

			if (b == false) {
				return ok(Json.toJson(new BaseJson(BaseResultType.FAILURE,
						BaseResultType.OPERATE_FAIL)));
			}

			result.setRe(BaseResultType.SUCCESS);
			result.setMsg("");

		} catch (Exception e) {
			HashMap<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController updatePeronInfo Exception", e);
			e.printStackTrace();
			return ok(Json.toJson(objMap));
		}
		return ok(Json.toJson(result));
	}

	/**
	 * 积分信息
	 * 
	 */
	public Result points(int page, int pageSize) {
		Map<String, Object> pMap = null;
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Integer siteId = mobileService.getWebSiteID();

			pMap = userSvc.getPoints(email, siteId, page, pageSize);

			if (null == pMap) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						BaseResultType.NODATA)));
			}

			pMap.put("re", BaseResultType.SUCCESS);
			pMap.put("msg", "");
		} catch (Exception e) {
			pMap = new HashMap<String, Object>();
			pMap.put("re", BaseResultType.EXCEPTION);
			pMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController points Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(pMap));
	}

	/**
	 * 消息列表
	 * 
	 */
	public Result msgList(int page, int pageSize) {
		Map<String, Object> pMap = null;
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);

			pMap = userSvc.getMyMessageList(email, page, pageSize);
			if (null == pMap) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						BaseResultType.NODATA)));
			}

			pMap.put("re", BaseResultType.SUCCESS);
			pMap.put("msg", "");
		} catch (Exception e) {
			pMap = new HashMap<String, Object>();
			pMap.put("re", BaseResultType.EXCEPTION);
			pMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController msgList Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(pMap));
	}

	/**
	 * 消息详情
	 * 
	 */
	public Result msgDtl(String mid, String table) {
		Map<String, Object> pMap = null;
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);

			pMap = userSvc.getMessageDtl(email, mid, table);
			if (null == pMap) {
				return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
						BaseResultType.NODATA)));
			}

			pMap.put("re", BaseResultType.SUCCESS);
			pMap.put("msg", "");
		} catch (Exception e) {
			pMap = new HashMap<String, Object>();
			pMap.put("re", BaseResultType.EXCEPTION);
			pMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController msgDtl Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(pMap));
	}

	private BaseJson validateUserForm(UserInfoForm userform) {
		BaseJson result = new BaseJson();
		if (ValidataUtils.validateNull(userform.getNickname()) == false
				|| ValidataUtils.validateLength(userform.getNickname(), 50) == false) {
			result.setRe(BaseResultType.NICK_NAME_FORMAT_ERROR_CODE);
			result.setMsg(BaseResultType.NICK_NAME_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(userform.getFname()) == false
				|| ValidataUtils.validateLength(userform.getFname(), 50) == false) {
			result.setRe(BaseResultType.FNAME_FORMAT_ERROR_CODE);
			result.setMsg(BaseResultType.FIRST_NAME_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(userform.getLname()) == false
				|| ValidataUtils.validateLength(userform.getLname(), 50) == false) {
			result.setRe(BaseResultType.FNAME_FORMAT_ERROR_CODE);
			result.setMsg(BaseResultType.FIRST_NAME_ERROR_MSG);
			return result;
		}
		if (userform.getGender() == null) {
			result.setRe(BaseResultType.GENDER_VALUE_NULL_ERROR_CODE);
			result.setMsg(BaseResultType.GENDER_IS_NULL_ERROR_MSG);
			return result;
		}
		if (userform.getGender() > 2 || userform.getGender() <= 0) {
			result.setRe(BaseResultType.GENDER_VALUE_ERROR_CODE);
			result.setMsg(BaseResultType.GENDER_VALUE_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(userform.getBirth()) == false) {
			result.setRe(BaseResultType.BIRTHDAY_VALUE_NULL_ERROR_CODE);
			result.setMsg(BaseResultType.BIRTHDAY_IS_NULL_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(userform.getBirth(), 8) == false) {
			result.setRe(BaseResultType.BIRTHDAY_FORMATE_ERROR_CODE);
			result.setMsg(BaseResultType.BIRTHDAY_FORMAT_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateNull(userform.getCountry()) == false) {
			result.setRe(BaseResultType.COUNTRY_VALUE_NULL_ERROR_CODE);
			result.setMsg(BaseResultType.COUNTRY_IS_NULL_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(userform.getCountry(), 3) == false) {
			result.setRe(BaseResultType.COUNTRY_FORMATE_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.COUNTRY_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		if (ValidataUtils.validateLength(userform.getAbout(), 3000) == false) {
			result.setRe(BaseResultType.ABOUT_FORMATE_LENGTH_OVER_ERROR_CODE);
			result.setMsg(BaseResultType.ABOUT_LENGTH_OVER_ERROR_MSG);
			return result;
		}
		result.setRe(BaseResultType.SUCCESS);
		return result;
	}

	/**
	 * 上传个人头像
	 * 
	 * 
	 * @return
	 */
	public Result uploadUserImg() {
		Map<String, Object> objMap = new HashMap<String, Object>();
		FileChannel fc = null;
		FileInputStream fis = null;
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Integer siteId = mobileService.getWebSiteID();
			MultipartFormData body = request().body().asMultipartFormData();

			List<FilePart> parts = body.getFiles();
			if (parts == null) {
				return ok(Json.toJson(new BaseJson(
						BaseResultType.UPLOAD_USER_IMG_NULL_ERROR_CODE,
						BaseResultType.UPLOAD_USER_IMG_IS_NULL_ERROR_MSG)));
			}
			if (parts.size() >= 2) {
				return ok(Json.toJson(new BaseJson(
						BaseResultType.UPLOAD_USER_IMG_FILE_OVER_ERROR_CODE,
						BaseResultType.UPLOAD_USER_IMG_FILE_OVER_ERROR_MSG)));
			}

			File file = parts.get(0).getFile();
			String contenttype = parts.get(0).getContentType();
			if (file.exists() == false) {
				return ok(Json.toJson(new BaseJson(
						BaseResultType.UPLOAD_USER_IMG_NOT_FILE_ERROR_CODE,
						BaseResultType.UPLOAD_USER_IMG_NOT_FILE_ERROR_MSG)));
			}

			fis = new FileInputStream(file);
			fc = fis.getChannel();
			if (ValidataUtils.checkContentType(contenttype) == false) {
				return ok(Json.toJson(new BaseJson(
						BaseResultType.UPLOAD_USER_IMG_CONTENT_TYPE_ERROR_CODE,
						BaseResultType.UPLOAD_USER_IMG_CONTENT_TYPE_ERROR_MSG)));
			}
			if (ValidataUtils.checkSize(fc.size(), 50) == false) {
				return ok(Json
						.toJson(new BaseJson(
								BaseResultType.UPLOAD_USER_IMG_FILE_SIZE_OVER_MAX_ERROR_CODE,
								BaseResultType.UPLOAD_USER_IMG_FILE_SIZE_OVER_MAX_ERROR_MSG)));
			}
			boolean b = userSvc.updatePhoto(email, siteId, contenttype, file);
			if (b == false) {
				return ok(Json.toJson(new BaseJson(BaseResultType.FAILURE,
						BaseResultType.OPERATE_FAIL)));
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", "");

		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", BaseResultType.EXCEPTIONMSG);
			Logger.error("UserController uploadUserImg Exception", e);
			e.printStackTrace();
		} finally {
			try {
				if (null != fc) {
					fc.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				Logger.error("UserController uploadUserImg IOException", e);
				e.printStackTrace();
			}
		}
		return ok(Json.toJson(objMap));
	}
}

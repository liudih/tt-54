package controllers.mobile.personal;

import interceptor.auth.LoginAuth;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;
import services.mobile.member.LoginService;
import services.mobile.personal.MyWishService;
import services.mobile.product.ProductService;
import utils.MsgUtils;
import utils.Page;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BasePageJson;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;
import dto.mobile.ProductLiteInfo;

@With(LoginAuth.class)
public class MyWishController extends TokenController {

	@Inject
	MyWishService myWishService;

	@Inject
	ProductService productService;

	@Inject
	LoginService loginService;

	public Result addWish(String gid) {
		try {
			if (StringUtils.isNotBlank(gid) && loginService.isLogin()) {
				String email = loginService.getLoginMemberEmail();
				boolean flag = myWishService.addWishProduct(gid, email);
				if (flag) {
					BaseJson result = new BaseJson();
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
					return ok(Json.toJson(result));
				}
			}
		} catch (Exception e) {
			Logger.error("wish Exception :", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
		return ok(Json.toJson(result));
	}

	public Result delWish(String gids) {
		try {
			if (StringUtils.isNotBlank(gids) && loginService.isLogin()) {
				String email = loginService.getLoginMemberEmail();
				String[] gidStrs = gids.split(",");
				boolean flag = myWishService.deleteWishs(
						Arrays.asList(gidStrs), email);
				if (flag) {
					BaseJson result = new BaseJson();
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
					return ok(Json.toJson(result));
				}
			}
		} catch (Exception e) {
			Logger.error("wish Exception :", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
		return ok(Json.toJson(result));
	}

	public Result getMyWishProducts(int p, int size) {
		try {
			if(size==0) size = 12;//默认值12
			if (loginService.isLogin()) {
				String email = loginService.getLoginMemberEmail();
				Page<ProductLiteInfo> pageResult = productService
						.getMyWishProducts(email, p, size);
				if (pageResult != null) {
					BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
					result.setList(pageResult.getList());
					result.setTotal(pageResult.getTotal());
					result.setSize(pageResult.getSize());
					result.setP(pageResult.getP());
					return ok(Json.toJson(result));
				}
			}
		} catch (Exception e) {
			Logger.error("wish Exception :", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		return ok(Json.toJson(result));
	}
}

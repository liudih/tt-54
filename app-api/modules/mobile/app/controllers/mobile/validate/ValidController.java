package controllers.mobile.validate;

import dto.mobile.CouponsInfo;
import entity.loyalty.IntegralUseRule;
import facades.cart.Cart;
import interceptor.auth.LoginAuth;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;
import services.base.utils.StringUtils;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.order.CartInfoService;
import services.mobile.validate.ValidService;
import utils.CommonDefn;
import utils.MsgUtils;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;

@With(LoginAuth.class)
public class ValidController extends TokenController {

	@Inject
	LoginService loginService;
	@Inject
	MobileService mobileService;
	@Inject
	ValidService checkService;
	// @Inject
	// CampaignExecutionService campaignExec;
	@Inject
	CartInfoService cartInfoService;

	/**
	 * 验证并使用积分
	 *
	 * @param costPoints
	 *            输入的积分
	 * @param cartId
	 *            购物车ID
	 * 
	 */
	public Result checkUsePoint(int costPoints, String cartId) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		// try {
		if (StringUtils.notEmpty(cartId) && costPoints > 0) {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			String currency = mobileService.getCurrency();
			Cart cart = cartInfoService.getCartById(cartId);
			if (null == cart) {
				objMap.put("re", BaseResultType.VALID_CART_NULL_EXCEPTION);
				objMap.put("msg", MsgUtils
						.msg(BaseResultType.VALID_CART_NULL_EXCEPTION_MSG));
				return ok(Json.toJson(objMap));
			}
			IntegralUseRule rule = checkService.getRule(email);
			if (null == rule) {
				objMap.put("re", BaseResultType.VALID_RULE_NULL_EXCEPTION);
				objMap.put("msg", MsgUtils
						.msg(BaseResultType.VALID_RULE_NULL_EXCEPTION_MSG));
				return ok(Json.toJson(objMap));
			}
			Double ponintsMoney = checkService.getPonintsMoney(costPoints,
					email, currency, rule);
			if (null == ponintsMoney) {
				objMap.put("re",
						BaseResultType.VALID_POINTS_MONEY_NULL_EXCEPTION);
				objMap.put(
						"msg",
						MsgUtils.msg(BaseResultType.VALID_POINTS_MONEY_NULL_EXCEPTION_MSG));
				return ok(Json.toJson(objMap));
			}

			boolean flag = checkService.usePoint(cartId, costPoints);
			if (flag) {
				objMap.put("re", BaseResultType.SUCCESS);
				objMap.put("msg", ponintsMoney);
				return ok(Json.toJson(objMap));
			}
		}
		objMap.put("re", BaseResultType.FAILURE);
		objMap.put("msg", MsgUtils.msg(BaseResultType.OPERATE_FAIL));
		// } catch (Exception e) {
		// objMap.put("re", BaseResultType.EXCEPTION);
		// objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
		// Logger.error("ValidController checkUsePoint Exception",
		// e.fillInStackTrace());
		// }
		return ok(Json.toJson(objMap));

	}

	/**
	 * 验证并使用推广码
	 *
	 * @param cartId
	 *            购物车ID
	 * @param code
	 *            输入的推广码号
	 * 
	 * 
	 */
	public Result checkSpCode(String cartId, String code) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			Integer siteId = mobileService.getWebSiteID();
			Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(),
					true);
			if (null == cart) {
				objMap.put("re", -1);
				objMap.put("msg", "cart null EXCEPTION");
				return ok(Json.toJson(objMap));
			}
			// List<ICampaignInstance> execd = campaignExec
			// .execute(new PromotionCodeUsage(cart, code, email, siteId));
			//
			// if (execd == null || execd.size() == 0) {
			// objMap.put("re", BaseResultType.FAILURE);
			// objMap.put("msg", BaseResultType.OPERATE_FAIL);
			// return ok(Json.toJson(objMap));
			// }
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", "");
			// objMap.put("execd", execd);
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("ValidController checkSpCode Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));
	}

	/**
	 * 获取订单可使用的优惠劵信息
	 *
	 * @param code
	 *            输入的优惠劵号
	 * @param cartId
	 *            购物车ID
	 * 
	 */
	public Result getUserOrderCoupon(String cartId) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			List<CouponsInfo> cuList = checkService.getUsefulCoupon(cartId,
					email);
			if (cuList == null || cuList.size() == 0) {
				objMap.put("list", cuList);
				objMap.put("re", BaseResultType.ERROR);
				objMap.put("msg", MsgUtils.msg(BaseResultType.NODATA));
			} else {
				objMap.put("list", cuList);
				objMap.put("re", BaseResultType.SUCCESS);
				objMap.put("msg", "");
			}
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("ValidController getUserOrderCoupon Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));

	}

	/**
	 * 验证并使用优惠劵
	 *
	 * @param code
	 *            输入的优惠劵号
	 * @param cartId
	 *            购物车ID
	 * 
	 */
	public Result useCoupon(String cartId, String code) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			String email = loginService
					.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
			boolean b = checkService.applyCoupon(cartId, email, code);
			Logger.error(cartId + "----------" + email + "------" + code);
			if (b == false) {
				objMap.put("re", BaseResultType.FAILURE);
				objMap.put("msg", MsgUtils.msg(BaseResultType.OPERATE_FAIL));
				return ok(Json.toJson(objMap));
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", "");
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("ValidController useCoupon Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));

	}

	/**
	 * 删除使用优惠劵
	 *
	 * 
	 */
	public Result deleteUseCoupon(String cartId, String code) {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		try {
			boolean b = checkService.deleteCoupon(cartId, code);
			if (b == false) {
				objMap.put("re", BaseResultType.FAILURE);
				objMap.put("msg", MsgUtils.msg(BaseResultType.OPERATE_FAIL));
				return ok(Json.toJson(objMap));
			}
			objMap.put("re", BaseResultType.SUCCESS);
			objMap.put("msg", "");
		} catch (Exception e) {
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			Logger.error("ValidController deleteUseCoupon Exception", e);
			e.printStackTrace();
		}
		return ok(Json.toJson(objMap));

	}

	/**
	 * 删除用户订单使用积分
	 *
	 * 
	 */
	public Result deleteUsePoint() {
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		// try {
		Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(),
				false);
		if (cart != null) {
			boolean b = checkService.delPoints(cart.getId());
			if (b == false) {
				objMap.put("re", BaseResultType.FAILURE);
				objMap.put("msg", MsgUtils.msg(BaseResultType.OPERATE_FAIL));
				return ok(Json.toJson(objMap));
			}
		}
		objMap.put("re", BaseResultType.SUCCESS);
		objMap.put("msg", "");
		// } catch (Exception e) {
		// objMap.put("re", BaseResultType.EXCEPTION);
		// objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
		// Logger.error("ValidController deleteUsePoint Exception", e);
		// e.printStackTrace();
		// }
		return ok(Json.toJson(objMap));
	}
}

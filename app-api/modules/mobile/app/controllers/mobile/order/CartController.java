package controllers.mobile.order;

import interceptor.auth.LoginAuth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.order.CartInfoService;
import services.mobile.order.OrderService;
import utils.MsgUtils;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;

public class CartController extends TokenController {

	@Inject
	CartInfoService cartService;

	@Inject
	OrderService orderService;

	@Inject
	MobileService mobileService;

	@Inject
	LoginService loginService;
	
	private final String ecurl = "h5/ec-cart";

	@With(LoginAuth.class)
	public Result showCart() {
		try {
			String uuid = mobileService.getUUID();
			if (StringUtils.isNotBlank(uuid)) {
				Map<String, Object> resultMap = cartService.viewCart(uuid);
				if (resultMap != null && !resultMap.isEmpty()) {
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("re", BaseResultType.SUCCESS);
					result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
					result.put("ecurl", ecurl);
					result.putAll(resultMap);
					return ok(Json.toJson(result));
				}
			}
		} catch (Exception e) {
			Logger.error("Cart Exception", e.fillInStackTrace());
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

	public Result simpleCart() {
		try {
			String uuid = mobileService.getUUID();
			if (StringUtils.isNotBlank(uuid)) {
				Map<String, Object> resultMap = cartService.viewCartItem(uuid);
				if (resultMap != null && !resultMap.isEmpty()) {
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("re", BaseResultType.SUCCESS);
					result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
					result.put("ecurl", ecurl);
					result.putAll(resultMap);
					return ok(Json.toJson(result));
				}
			}
		} catch (Exception e) {
			Logger.error("Cart Exception", e.fillInStackTrace());
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

	@With(LoginAuth.class)
	public Result shippingMethods(int aid) {
		try {
			String uuid = mobileService.getUUID();
			if (StringUtils.isNotBlank(uuid)) {
				Map<String, Object> resultMap = cartService.getShippingMethods(
						uuid, aid);
				if (resultMap != null && !resultMap.isEmpty()) {
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("re", BaseResultType.SUCCESS);
					result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
					result.putAll(resultMap);
					return ok(Json.toJson(result));
				}
			}
		} catch (Exception e) {
			Logger.error("Cart Exception", e.fillInStackTrace());
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

	public Result deleteItemList(String iid, String gid) {
		try {
			String uuid = mobileService.getUUID();
			if (StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(iid)
					&& StringUtils.isNotBlank(gid)) {
				boolean isdelete = cartService.deleteItemList(uuid, iid, gid,
						false);
				if (isdelete) {
					Map<String, Object> resultMap = cartService
							.viewCartItem(uuid);
					if (resultMap != null && !resultMap.isEmpty()) {
						Map<String, Object> result = new HashMap<String, Object>();
						result.put("re", BaseResultType.SUCCESS);
						result.put("msg",
								MsgUtils.msg(BaseResultType.SUCCESSMSG));
						result.putAll(resultMap);
						return ok(Json.toJson(result));
					} else {
						BaseJson result = new BaseJson();
						result.setRe(BaseResultType.ERROR);
						result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
						return ok(Json.toJson(result));
					}
				}
			}
		} catch (Exception e) {
			Logger.error("Cart Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.FAILURE);
		result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
		return ok(Json.toJson(result));
	}

	public Result updateItemQty(String iid, int qty) {
		try {
			String uuid = mobileService.getUUID();
			if (StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(iid)) {
				Map map = cartService.editNum(uuid, iid, qty);
				if ("success".equals(map.get("res"))) {
					Map<String, Object> resultMap = cartService
							.viewCartItem(uuid);
					if (resultMap != null && !resultMap.isEmpty()) {
						Map<String, Object> result = new HashMap<String, Object>();
						result.put("re", BaseResultType.SUCCESS);
						result.put("msg",
								MsgUtils.msg(BaseResultType.SUCCESSMSG));
						result.putAll(resultMap);
						return ok(Json.toJson(result));
					} else {
						BaseJson result = new BaseJson();
						result.setRe(BaseResultType.ERROR);
						result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
						return ok(Json.toJson(result));
					}
				} else if ("notenough".equals(map.get("res"))) {
					BaseJson result = new BaseJson();
					result.setRe(BaseResultType.NOTENOUGH);
					result.setMsg(MsgUtils.msg(BaseResultType.NOT_ENOUGH));
				}
			}
		} catch (Exception e) {
			Logger.error("Cart Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.FAILURE);
		result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
		return ok(Json.toJson(result));
	}

	public Result addCartItem(String gid, int qty) {
		BaseJson result = new BaseJson();
		try {
			String uuid = mobileService.getUUID();
			if (StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(gid)
					&& qty > 0) {
				Map map = cartService.addCartItem(uuid, gid, qty);
				if ("success".equals(map.get("res"))) {
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(cartService.getItemCount(uuid) + "");
				} else if ("notenough".equals(map.get("res"))) {
					result.setRe(BaseResultType.NOTENOUGH);
					result.setMsg(MsgUtils.msg(BaseResultType.NOT_ENOUGH));
				} else {
					result.setRe(BaseResultType.FAILURE);
					result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
				}
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Cart Exception", e.fillInStackTrace());
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		return ok(Json.toJson(result));
	}
}

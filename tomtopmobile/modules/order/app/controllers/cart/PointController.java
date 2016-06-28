package controllers.cart;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.loyalty.IPointsService;
import valueobjects.base.LoginContext;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

/**
 * 积分使用
 * 
 * @author lijun
 *
 */
public class PointController extends Controller {

	@Inject
	IPointsService pointsService;

	@Inject
	FoundationService foundationService;

	/**
	 * 积分使用
	 * 
	 * @author lijun
	 * @param point
	 *            要使用的积分
	 * @param cartId
	 *            购物车id
	 * @return
	 */
	public Result usePoint() {
		// 如果用户没有登录那么强制去登录
		LoginContext loginCtx = foundationService.getLoginContext();
		if (loginCtx == null || !loginCtx.isLogin()) {
			return badRequest();
		}
		response().setContentType("application/json");
		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest();
		}
		JSONObject feedback = new JSONObject();
		try {
			int point = json.get("point").asInt();
			String cartId = json.get("cartId").asText();
			boolean succeed = pointsService.usePoint(point, cartId);
			Logger.debug("购物车{}积分{}使用成功:{}", cartId, point, succeed);
			feedback.put("succeed", succeed);
		} catch (Exception e) {
			feedback.put("succeed", false);
			Logger.debug("购物车积分使用失败", e);
		}
		return ok(Json.toJson(feedback));
	}

	/**
	 * 取消积分的使用
	 * 
	 * @author lijun
	 * @return
	 */
	public Result cancelUsedPoint() {
		// 如果用户没有登录那么强制去登录
		LoginContext loginCtx = foundationService.getLoginContext();
		if (loginCtx == null || !loginCtx.isLogin()) {
			return badRequest();
		}
		response().setContentType("application/json");
		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest();
		}
		JSONObject feedback = new JSONObject();
		try {
			String cartId = json.get("cartId").asText();
			boolean succeed = pointsService.cancelUsedPoint(cartId);
			Logger.debug("购物车{}取消积分成功:{}", cartId, succeed);
			feedback.put("succeed", succeed);
		} catch (Exception e) {
			feedback.put("succeed", false);
			Logger.debug("购物车积分取消失败", e);
		}
		return ok(Json.toJson(feedback));
	}
}

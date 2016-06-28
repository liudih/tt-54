package controllers.loyalty;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.base.utils.Utils;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignExecutionService;
import services.cart.ICartLifecycleService;
import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponMainService;
import valueobjects.base.LoginContext;
import valueobjects.base.Page;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import entity.loyalty.Coupon;
import enums.loyalty.coupon.manager.CouponRuleBack;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import extensions.loyalty.orderxtras.CouponUseExtrasProvider;

/**
 * Account->My Coupons
 * 
 * @author lijun
 *
 */
@Authenticated(MemberLoginAuthenticator.class)
public class MyCoupon extends Controller {
	@Inject
	private ICouponMainService service;

	@Inject
	private FoundationService fservice;

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	CampaignExecutionService campaignExec;

	@Inject
	CouponUseExtrasProvider extrasProvider;

	@Inject
	ICartCouponService ccService;

	@Inject
	CurrencyService currencyService;

	public Result list(int page, int pageSize) {

		String tab = request().getQueryString("tab");
		LoginContext context = fservice.getLoginContext();
		String userEmail = context.getMemberID();
		if (StringUtils.isEmpty(userEmail)) {
			return internalServerError();
		}
		Page<Coupon> unused = null;
		Page<Coupon> used = null;
		if (tab == null) {
			unused = service.selectMyCouponUnusedForPage(page, pageSize,
					userEmail);
			used = service.selectMyCouponUsedForPage(page, pageSize, userEmail);
		} else if ("unused".equals(tab)) {
			unused = service.selectMyCouponUnusedForPage(page, pageSize,
					userEmail);
			used = service.selectMyCouponUsedForPage(1, 15, userEmail);
		} else if ("used".equals(tab)) {
			unused = service.selectMyCouponUnusedForPage(1, 15, userEmail);
			used = service.selectMyCouponUsedForPage(page, pageSize, userEmail);
		}

		service.convert(unused.getList());
		return ok(views.html.loyalty.coupon.list.render(unused, used, tab));
	}

	/**
	 * 用户在购物车下订单的时候回来请求可用的优惠券
	 * 
	 * @return
	 */
	public Result getMyUsableCoupon() {
		String cartId = request().getQueryString("cartId");
		if (StringUtils.isEmpty(cartId)) {
			badRequest();
		}

		// 获取当前登录用户的账号
		LoginContext loginContext = fservice.getLoginContext();
		String email = loginContext.getMemberID();
		if (StringUtils.isEmpty(email)) {
			return badRequest();
		}
		WebContext webContext = ContextUtils.getWebContext(ctx());
		// 调用查询可用优惠券接口
		List<valueobjects.loyalty.Coupon> usableRule = service
				.getMyUsableCoupon(email, cartId, webContext);
		// 构造返回前台的json数据
		JSONArray result = new JSONArray();
		usableRule.forEach(c -> {
			JSONObject json = new JSONObject();
			if (c.isCash()) {
				json.put("value", Utils.money(c.getAmount()) + " "
						+ c.getCurrency().getCsymbol());
			} else {
				double discount = c.getPercent();
				json.put("value", Utils.money(discount) + "% OFF");
			}
			json.put("code", c.getCode());
			result.add(json);
		});
		return ok(result.toJSONString()).as("application/json");
	}

	/**
	 * 应用购物券
	 * 
	 * @return
	 */
	public Result apply(String cartId, String code) {
		if (StringUtils.isEmpty(cartId)) {
			return badRequest();
		}
		if (StringUtils.isEmpty(code)) {
			return badRequest();
		}
		// 获取当前登录用户的账号
		LoginContext loginContext = fservice.getLoginContext();
		String email = loginContext.getMemberID();
		if (StringUtils.isEmpty(email)) {
			return badRequest();
		}
		WebContext webContext = fservice.getWebContext();
		// 调用应用优惠券接口
		boolean result = service.applyCoupon(email, cartId, code, webContext);

		if (result) {
			return ok();
		} else {
			return badRequest();
		}
	}

	/**
	 * 解除优惠券和购物车的绑定
	 * 
	 * @return
	 */
	public Result delCartCoupon() {
		JsonNode paras = request().body().asJson();
		if (paras == null) {
			return badRequest();
		}
		JsonNode cartIdNode = paras.findValue("cartId");
		JsonNode codeNode = paras.findValue("code");
		if (cartIdNode == null) {
			return badRequest();
		}
		if (codeNode == null) {
			return badRequest();
		}
		String cartId = cartIdNode.asText();
		String code = codeNode.asText();
		Logger.debug(cartId);
		Logger.debug(code);
		if (StringUtils.isEmpty(cartId)) {
			return badRequest();
		}
		if (StringUtils.isEmpty(code)) {
			return badRequest();
		}
		// 调用取消使用优惠券接口
		service.delCartCoupon(cartId, code);

		JsonNode result = Json.parse("{\"succeed\" : true}");
		Logger.debug(result.toString());
		return ok(result);
	}
}

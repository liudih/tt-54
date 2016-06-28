package controllers.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICurrencyService;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.cart.ICartServices;
import services.loyalty.IPointsService;
import services.order.ICheckoutService;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyCoupon;
import valueobjects.loyalty.LoyaltyPrefer;
import context.WebContext;

public class LoyaltyController extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	ICheckoutService checkoutService;

	@Inject
	ICurrencyService currencyService;
	
	@Inject
	ICartServices cartServices;
	
	@Inject
	IPointsService pointsService;
	
	@Inject
	extensions.loyalty.IOrderLoyaltyProvider loyaltyService;
	
	public static final String LOYALTY_PREFER = "loyalty";
	public static final String LOYALTY_TYPE_PROMO = "promo";
	public static final String LOYALTY_TYPE_COUPON = "coupon";
	public static final String LOYALTY_TYPE_POINT = "point";

	private List<CartItem> getCartItems() {
		int site = foundation.getSiteID();
		int lang = foundation.getLanguage();
		String currency = foundation.getCurrency();
		List<CartItem> newCartItems = cartServices.getAllItemsCurrentStorageid(site, lang, currency);
		return newCartItems;
	}

	/**
	 * 应用coupon
	 * 
	 * @return
	 */
	public Result applyCoupon(String code) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		LoyaltyPrefer currentPrefer = getCurrentPrefer();
		if (null != currentPrefer) {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}
		String email = foundation.getLoginContext().getMemberID();
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}
		LoyaltyPrefer loyaltyPrefer = loyaltyService.applyCoupon(email, code);

		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			ctx.response().setCookie(LOYALTY_PREFER,
					LOYALTY_TYPE_COUPON + ":" + code, 365 * 24 * 3600, "/");
			mjson.put("result", "success");
			mjson.put("data", loyaltyPrefer);
		} else {
			mjson.put("result", "error");
		}

		return ok(Json.toJson(mjson));
	}

	/**
	 * 应用推广码
	 * 
	 * @return
	 */
	public Result applyPromo(String code) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		LoyaltyPrefer currentPrefer = getCurrentPrefer();
		if (null != currentPrefer) {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}
		String email = foundation.getLoginContext().getMemberID();
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}

		LoyaltyPrefer loyaltyPrefer = loyaltyService.applyPromo(email, code);

		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			CookieUtils.setCookie(LOYALTY_PREFER, LOYALTY_TYPE_PROMO + ":"
					+ code, ctx);
			mjson.put("result", "success");
			mjson.put("data", loyaltyPrefer);
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	/**
	 * 查看当前coupon,promo优惠
	 * 
	 * @return
	 */
	public LoyaltyPrefer getCurrentPrefer() {

		String email = foundation.getLoginContext().getMemberID();
		if (email == null || email.length() == 0) {
			return null;
		}
		LoyaltyPrefer loyaltyPrefer = loyaltyService.getCurrentPrefer(email);
		if (loyaltyPrefer.isSuccess()) {
			return loyaltyPrefer;
		}
		return null;
	}

	/**
	 * 获取可用的coupon
	 * 
	 * @return
	 */
	public Result getUsableCoupon() {
		String email = foundation.getLoginContext().getMemberID();
		if (email == null || email.length() == 0) {
			return badRequest();
		}

		List<CartItem> cartItems = getCartItems();
		WebContext webContext = foundation.getWebContext();
		List<LoyaltyCoupon> coupons = loyaltyService.getMyUsableCoupon(email,
				cartItems, webContext);
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (null != coupons && coupons.size() > 0) {
			mjson.put("result", "success");
			mjson.put("data", coupons);
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	/**
	 * 应用积分
	 * 
	 * @return
	 */
	public Result applyPoints(Integer costpoints) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		String email = foundation.getLoginContext().getMemberID();
		if (StringUtils.isEmpty(email) || null == costpoints || costpoints <= 0) {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}

		LoyaltyPrefer loyaltyPrefer = loyaltyService.applyPoints(email,
				costpoints);
		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			// ctx.response().setCookie(LOYALTY_TYPE_POINT,
			// costpoints + "", 365 * 24 * 3600, "/");
			CookieUtils.setCookie(LOYALTY_TYPE_POINT, costpoints + "", ctx);
			mjson.put("result", "success");
			mjson.put("data", loyaltyPrefer);
		} else {
			mjson.put("result", "error");
		}

		return ok(Json.toJson(mjson));
	}

	public Result getAllCurrentPrefer() {
		List<LoyaltyPrefer> loyaltyPrefers = loyaltyService
				.getAllCurrentPrefer();
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (null != loyaltyPrefers && loyaltyPrefers.size() > 0) {
			mjson.put("result", "success");
			mjson.put("data", loyaltyPrefers);
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	/**
	 * 取消应用优惠
	 */
	public Result undoCurrentPrefer() {
		Context ctx = Context.current();
		if (ctx.request().cookie(LOYALTY_PREFER) != null) {
			CookieUtils.removeCookie(LOYALTY_PREFER, ctx);
			// ctx.response().setCookie(LOYALTY_PREFER,
			// "", 0, "/");
		}
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	/**
	 * 取消应用积分
	 */
	public Result undoCurrentPoint() {
		Context ctx = Context.current();
		if (ctx.request().cookie(LOYALTY_TYPE_POINT) != null) {
			CookieUtils.removeCookie(LOYALTY_TYPE_POINT, ctx);
			// ctx.response().setCookie(LOYALTY_TYPE_POINT,
			// "", 0, "/");
		}
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	/**
	 * 获取可用积分
	 * 
	 * @param email
	 * @return
	 */
	public Result getMyUsablePoint() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		String email = foundation.getLoginContext().getMemberID();
		if (StringUtils.isEmpty(email)) {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}
		List<CartItem> cartItems = this.getCartItems();
		Double total = checkoutService.subToatl(cartItems);
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
		List<LoyaltyPrefer> allPrefers = loyaltyService.getAllCurrentPrefer();
		for (LoyaltyPrefer lp : allPrefers) {
			duti = duti.add(lp.getValue());
		}
		Double usableTotal = duti.add(total).doubleValue();
		usableTotal = usableTotal > 0 ? usableTotal : 0;
		Double USDTotal = currencyService.exchange(usableTotal, cartItems
				.get(0).getPrice().getCurrency(), "USD");
		// 保留2位小数
		USDTotal = Math.round(USDTotal * 100) * 0.01d;
		USDTotal = USDTotal * 100;
		int totalPoints = USDTotal.intValue();
		int points = pointsService.getUsefulPoints(email,
				foundation.getSiteID());
		int avaliable = 0;
		avaliable = points > totalPoints ? totalPoints : points;
		mjson.put("result", "success");
		mjson.put("data", avaliable);
		mjson.put("total", points);
		return ok(Json.toJson(mjson));
	}

}

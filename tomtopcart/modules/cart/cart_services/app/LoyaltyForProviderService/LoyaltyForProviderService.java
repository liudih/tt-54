package LoyaltyForProviderService;

import java.util.List;

import javax.inject.Inject;
import play.mvc.Http.Context;

import context.WebContext;

import extensions.loyalty.IOrderLoyaltyProvider;

import services.base.FoundationService;

import services.base.utils.CookieUtils;
import services.cart.ICartServices;

import services.loyalty.IPointsService;

import services.loyalty.IPreferService;

import services.loyalty.coupon.ICouponService;

import services.order.ICheckoutService;

import valueobjects.cart.CartItem;

import valueobjects.loyalty.LoyaltyCoupon;

import valueobjects.loyalty.LoyaltyPrefer;

public class LoyaltyForProviderService implements IOrderLoyaltyProvider {
	@Inject
	ICouponService couponService;
	@Inject
	ICartServices cartService;
	@Inject
	FoundationService foundation;
	@Inject
	IPointsService pointsService;
	@Inject
	IPreferService preferService;
	@Inject
	ICheckoutService checkoutService;
	public static final String DOMAIN = "tomtop.com";

	@Override
	public LoyaltyPrefer applyCoupon(String email, String code) {
		Integer websiteid = foundation.getSiteID();
		Integer language = foundation.getLanguage();
		String currency = foundation.getCurrency();
		List<CartItem> cartItems = cartService.getAllItemsCurrentStorageid(websiteid, language,
				currency);
		// 远程调用应用优惠券
		WebContext webContext = foundation.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = couponService.applyCoupon(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) > totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			CookieUtils.setCookie(ICouponService.LOYALTY_PREFER,
					ICouponService.LOYALTY_TYPE_COUPON + ":" + code, ctx);
		}
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer applyPromo(String email, String code) {
		Integer websiteid = foundation.getSiteID();
		Integer language = foundation.getLanguage();
		String currency = foundation.getCurrency();
		List<CartItem> cartItems = cartService.getAllItemsCurrentStorageid(websiteid, language,
				currency);
		WebContext webContext = foundation.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = couponService.applyPromo(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			CookieUtils.setCookie(ICouponService.LOYALTY_PREFER,
					ICouponService.LOYALTY_TYPE_PROMO + ":" + code, ctx);
		}
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer getCurrentPrefer(String email) {
		WebContext webContext = foundation.getWebContext();
		Integer websiteid = foundation.getSiteID();
		Integer lan = foundation.getLanguage();
		String currency = foundation.getCurrency();
		List<CartItem> cartItems = cartService.getAllItemsCurrentStorageid(websiteid, lan,
				currency);
		// webContext.setCurrencyCode("USD");
		LoyaltyPrefer loyaltyPrefer = couponService.getCurrentPrefer(email,
				cartItems, webContext);
		return loyaltyPrefer;
	}

	@Override
	public void undoCurrentPrefer() {
		Context ctx = Context.current();
		if (ctx.request().cookie(ICouponService.LOYALTY_PREFER) != null) {
			CookieUtils.removeCookie(ICouponService.LOYALTY_PREFER, ctx);
		}
	}

	@Override
	public void undoCurrentPoint() {
		Context ctx = Context.current();
		if (ctx.request().cookie(ICouponService.LOYALTY_TYPE_POINT) != null) {
			CookieUtils.removeCookie(ICouponService.LOYALTY_TYPE_POINT, ctx);
		}
	}

	@Override
	public LoyaltyPrefer applyPoints(String email, Integer costpoints) {
		Integer websiteid = foundation.getSiteID();
		Integer language = foundation.getLanguage();
		String currency = foundation.getCurrency();
		List<CartItem> cartItems = cartService.getAllItemsCurrentStorageid(websiteid, language,
				currency);

		WebContext webContext = foundation.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = pointsService.applyPoints(email,
				cartItems, costpoints, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				return new LoyaltyPrefer();
			}
		}
		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			CookieUtils.setCookie(ICouponService.LOYALTY_TYPE_POINT, costpoints
					+ "", ctx);
		}
		return loyaltyPrefer;
	}

	@Override
	public List<LoyaltyPrefer> getAllCurrentPrefer() {
		String email = foundation.getLoginContext().getMemberID();
//		if (email == null || email.length() == 0) {
//			return null;
//		}
		Integer websiteid = foundation.getSiteID();
		Integer language = foundation.getLanguage();
		String currency = foundation.getCurrency();
		List<CartItem> cartItems = cartService.getAllItemsCurrentStorageid(websiteid, language,
				currency);
		WebContext webContext = foundation.getWebContext();
		List<LoyaltyPrefer> loyaltyPrefers = preferService.getAllPreferByEmail(
				email, cartItems, webContext);
		Context ctx = Context.current();
		if (ctx.request().cookie(ICouponService.LOYALTY_PREFER) != null) {
			CookieUtils.removeCookie(ICouponService.LOYALTY_PREFER, ctx);
		}
		if (ctx.request().cookie(ICouponService.LOYALTY_TYPE_POINT) != null) {
			CookieUtils.removeCookie(ICouponService.LOYALTY_TYPE_POINT, ctx);
		}
		if (null != loyaltyPrefers && loyaltyPrefers.size() > 0) {
			loyaltyPrefers.forEach(c -> {
				if (c.getPreferType()
						.equals(ICouponService.LOYALTY_TYPE_COUPON)) {
					CookieUtils.setCookie(
							ICouponService.LOYALTY_PREFER,
							ICouponService.LOYALTY_TYPE_COUPON + ":"
									+ c.getCode(), ctx);
				} else if (c.getPreferType().equals(
						ICouponService.LOYALTY_TYPE_PROMO)) {
					CookieUtils.setCookie(
							ICouponService.LOYALTY_PREFER,
							ICouponService.LOYALTY_TYPE_PROMO + ":"
									+ c.getCode(), ctx);
				} else if (c.getPreferType().equals(
						ICouponService.LOYALTY_TYPE_POINT)) {
					CookieUtils.setCookie(ICouponService.LOYALTY_TYPE_POINT,
							c.getCode(), ctx);
				}
			});
		}
		return loyaltyPrefers;
	}

	@Override
	public List<LoyaltyCoupon> getMyUsableCoupon(String email,
			List<CartItem> cartItems, WebContext webCtx) {
		return couponService.getMyUsableCoupon(email, cartItems, webCtx);
	}
}

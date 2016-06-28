package services.loyalty;

import java.util.List;
import javax.inject.Inject;
import org.springframework.beans.BeanUtils;
import com.google.api.client.util.Lists;
import play.mvc.Http.Context;
import context.WebContext;
import extensions.order.IOrderLoyaltyProvider;
import facades.cart.Cart;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import services.base.utils.DoubleCalculateUtils;
import services.cart.ICartLifecycleService;
import services.cart.ICartServices;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.ICouponService;
import services.order.ICheckoutService;
import valueobjects.cart.CartItem;
import valueobjects.cart.Price;
import valueobjects.loyalty.LoyaltyCoupon;
import valueobjects.loyalty.LoyaltyPrefer;

public class LoyaltyForProviderService implements IOrderLoyaltyProvider {

	@Inject
	ICouponMainService couponService;

	@Inject
	ICartServices cartService;

	@Inject
	FoundationService foundation;

	@Inject
	IPointsService pointsService;

	@Inject
	IPreferService preferService;

	@Inject
	LoyaltyOrderCall loyaltyOrderCall;

	@Inject
	ICheckoutService checkoutService;

	@Inject
	ICartLifecycleService cartLifecycle;

	private List<CartItem> getCartItemsByCartId(String cartId) {
		Cart cart = cartLifecycle.getCart(cartId);
		List<valueobjects.order_api.cart.CartItem> oldCartItems = cart
				.getAllItems();
		List<CartItem> newCartItems = Lists.newArrayList();
		if (oldCartItems != null && oldCartItems.size() > 0) {
			oldCartItems.forEach(c -> {
				CartItem newCartItem = new CartItem();
				BeanUtils.copyProperties(c, newCartItem);
				Price newPrice = new Price();
				BeanUtils.copyProperties(c.getPrice(), newCartItem);
				BeanUtils.copyProperties(c.getPrice(), newPrice);
				newCartItem.setPrice(newPrice);
				newCartItems.add(newCartItem);
			});
		}
		return newCartItems;
	}

	private List<CartItem> getCartItemsByCookie() {
		Integer websiteid = foundation.getSiteID();
		Integer language = foundation.getLanguage();
		String currency = foundation.getCurrency();
		List<CartItem> cartItems = cartService.getAllItemsCurrentStorageid(websiteid, language,
				currency);
		return cartItems;
	}

	@Override
	public LoyaltyPrefer applyCoupon(String email, String code) {

		List<CartItem> cartItems = this.getCartItemsByCookie();
		// 远程调用应用优惠券
		WebContext webContext = foundation.getWebContext();

		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = loyaltyOrderCall
				.getAllPreferByEmail(email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}

		LoyaltyPrefer loyaltyPrefer = couponService.applyCoupon(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				LoyaltyPrefer fail = new LoyaltyPrefer();
				fail.setErrorMessage(play.i18n.Messages
						.get("loyalty.errorMessage.noRule"));
				return fail;
			}
		}
		Context ctx = Context.current();
		if (loyaltyPrefer.isSuccess()) {
			CookieUtils.setCookie(ICouponService.LOYALTY_PREFER,
					ICouponService.LOYALTY_TYPE_COUPON + ":" + code, ctx);
		}
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer applyPromo(String email, String code) {
		List<CartItem> cartItems = this.getCartItemsByCookie();
		WebContext webContext = foundation.getWebContext();

		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = loyaltyOrderCall
				.getAllPreferByEmail(email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}

		LoyaltyPrefer loyaltyPrefer = couponService.applyPromo(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				LoyaltyPrefer fail = new LoyaltyPrefer();
				fail.setErrorMessage(play.i18n.Messages
						.get("loyalty.errorMessage.noRule"));
				return fail;
			}
		}
		Context ctx = Context.current();
		if (loyaltyPrefer.isSuccess()) {
			CookieUtils.setCookie(ICouponService.LOYALTY_PREFER,
					ICouponService.LOYALTY_TYPE_PROMO + ":" + code, ctx);
		}
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer getCurrentPrefer(String email) {
		WebContext webContext = foundation.getWebContext();
		List<CartItem> cartItems = this.getCartItemsByCookie();
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

		List<CartItem> cartItems = this.getCartItemsByCookie();

		WebContext webContext = foundation.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = loyaltyOrderCall
				.getAllPreferByEmail(email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = pointsService.applyPoints(email,
				cartItems, costpoints, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				LoyaltyPrefer fail = new LoyaltyPrefer();
				fail.setErrorMessage(play.i18n.Messages
						.get("loyalty.point.errorMessage.invalid"));
				return fail;
			}
		}
		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			if (loyaltyPrefer.isSuccess()) {
				CookieUtils.setCookie(ICouponService.LOYALTY_TYPE_POINT,
						costpoints + "", ctx);
			}
		}
		return loyaltyPrefer;
	}

	@Override
	public List<LoyaltyPrefer> getAllCurrentPrefer() {
		String email = foundation.getLoginContext().getMemberID();
		if (email == null || email.length() == 0) {
			return null;
		}

		List<CartItem> cartItems = this.getCartItemsByCookie();

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
							c.getCode() + "", ctx);
				}
			});
		}
		return loyaltyPrefers;
	}

	@Override
	public List<LoyaltyCoupon> getMyUsableCoupon(String email,
			List<CartItem> cartItems, WebContext webCtx) {
		List<LoyaltyCoupon> coupons = couponService.getMyUsableCoupon(email,
				cartItems, webCtx);
		return coupons;
	}

	@Override
	public int getMyUsablePoint(String email) {
		int siteID = foundation.getSiteID();
		return pointsService.getUsefulPoints(email, siteID);
	}

	@Override
	public LoyaltyPrefer applyCoupon(String cartId, String email, String code) {
		List<CartItem> cartItems = this.getCartItemsByCartId(cartId);
		// 远程调用应用优惠券
		WebContext webContext = foundation.getWebContext();

		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = loyaltyOrderCall
				.getAllPreferByEmail(email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}

		LoyaltyPrefer loyaltyPrefer = couponService.applyCoupon(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				LoyaltyPrefer fail = new LoyaltyPrefer();
				fail.setErrorMessage(play.i18n.Messages
						.get("loyalty.errorMessage.noRule"));
				return fail;
			}
		}
		Context ctx = Context.current();
		if (loyaltyPrefer.isSuccess()) {
			CookieUtils.setCookie(ICouponService.LOYALTY_PREFER,
					ICouponService.LOYALTY_TYPE_COUPON + ":" + code, ctx);
		}
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer applyPromo(String cartId, String email, String code) {
		List<CartItem> cartItems = this.getCartItemsByCartId(cartId);
		WebContext webContext = foundation.getWebContext();

		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = loyaltyOrderCall
				.getAllPreferByEmail(email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}

		LoyaltyPrefer loyaltyPrefer = couponService.applyPromo(email,
				cartItems, code, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				LoyaltyPrefer fail = new LoyaltyPrefer();
				fail.setErrorMessage(play.i18n.Messages
						.get("loyalty.errorMessage.noRule"));
				return fail;
			}
		}
		Context ctx = Context.current();
		if (loyaltyPrefer.isSuccess()) {
			CookieUtils.setCookie(ICouponService.LOYALTY_PREFER,
					ICouponService.LOYALTY_TYPE_PROMO + ":" + code, ctx);
		}
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer getCurrentPrefer(String cartId, String email) {
		WebContext webContext = foundation.getWebContext();
		List<CartItem> cartItems = this.getCartItemsByCartId(cartId);
		LoyaltyPrefer loyaltyPrefer = couponService.getCurrentPrefer(email,
				cartItems, webContext);
		return loyaltyPrefer;
	}

	@Override
	public LoyaltyPrefer applyPoints(String cartId, String email,
			Integer costpoints) {
		List<CartItem> cartItems = this.getCartItemsByCartId(cartId);

		WebContext webContext = foundation.getWebContext();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		// 当优惠价格大于商品总价则应用失败
		Double currentPreferValue = 0D;
		List<LoyaltyPrefer> preferCurrent = loyaltyOrderCall
				.getAllPreferByEmail(email, cartItems, webContext);
		if (null != preferCurrent && preferCurrent.size() > 0) {
			for (int i = 0; i < preferCurrent.size(); i++) {
				currentPreferValue += preferCurrent.get(i).getValue();
			}
		}
		LoyaltyPrefer loyaltyPrefer = pointsService.applyPoints(email,
				cartItems, costpoints, webContext);
		if (loyaltyPrefer.isSuccess()) {
			if ((-loyaltyPrefer.getValue() - currentPreferValue) >= totalPrice) {
				LoyaltyPrefer fail = new LoyaltyPrefer();
				fail.setErrorMessage(play.i18n.Messages
						.get("loyalty.point.errorMessage.invalid"));
				return fail;
			}
		}
		if (loyaltyPrefer.isSuccess()) {
			Context ctx = Context.current();
			if (loyaltyPrefer.isSuccess()) {
				CookieUtils.setCookie(ICouponService.LOYALTY_TYPE_POINT,
						costpoints + "", ctx);
			}
		}
		return loyaltyPrefer;
	}

	@Override
	public List<LoyaltyPrefer> getAllCurrentPrefer(String cartId) {
		String email = foundation.getLoginContext().getMemberID();
		if (email == null || email.length() == 0) {
			return null;
		}

		List<CartItem> cartItems = this.getCartItemsByCartId(cartId);

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
							c.getCode() + "", ctx);
				}
			});
		}
		return loyaltyPrefers;
	}

	@Override
	public List<LoyaltyCoupon> getMyUsableCoupon(String email, String cartId,
			WebContext webCtx) {
		List<CartItem> cartItems = this.getCartItemsByCartId(cartId);
		List<LoyaltyCoupon> coupons = couponService.getMyUsableCoupon(email,
				cartItems, webCtx);
		return coupons;
	}

	@Override
	public Double getAllCurrentPreferValue(String cartId) {
		List<LoyaltyPrefer> loyaltyPrefers = this.getAllCurrentPrefer(cartId);
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);

		if (null != loyaltyPrefers && loyaltyPrefers.size() > 0) {
			for (int i = 0; i < loyaltyPrefers.size(); i++) {
				if (loyaltyPrefers.get(i).getPreferType()
						.equals(ICouponService.LOYALTY_TYPE_COUPON)) {
					duti = duti.add(loyaltyPrefers.get(i).getValue());
				} else if (loyaltyPrefers.get(i).getPreferType()
						.equals(ICouponService.LOYALTY_TYPE_PROMO)) {
					duti = duti.add(loyaltyPrefers.get(i).getValue());
				} else if (loyaltyPrefers.get(i).getPreferType()
						.equals(ICouponService.LOYALTY_TYPE_POINT)) {
					duti = duti.add(loyaltyPrefers.get(i).getValue());
				}
			}
		}
		return duti.doubleValue();
	}

}

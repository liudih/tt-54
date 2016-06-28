package services.loyalty;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Http.Context;
import services.base.utils.CookieUtils;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.ICouponService;
import services.order.ICheckoutService;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyPrefer;
import context.WebContext;
import extensions.order.IPreferProvider;

/**
 * 订单付款成功后把相应的优惠信息设置为已用
 * 
 * @author xiaoch
 *
 */
public class LoyaltyOrderCall implements IPreferProvider, IPreferService {

	@Inject
	ICouponMainService couponService;

	@Inject
	IPointsService pointService;

	@Inject
	ICheckoutService checkoutService;

	@Override
	public List<LoyaltyPrefer> getAllPreferByEmail(String email,
			List<CartItem> cartItems, WebContext webCtx) {
		List<LoyaltyPrefer> prefers = new LinkedList<LoyaltyPrefer>();
		// 获取商品总价
		Double totalPrice = checkoutService.subToatl(cartItems);
		Context ctx = Context.current();
		if (ctx.request().cookie(ICouponService.LOYALTY_PREFER) != null) {

			String loyaltycookie = ctx.request()
					.cookie(ICouponService.LOYALTY_PREFER).value();
			String[] loyaltyArray = org.apache.commons.lang3.StringUtils.split(
					loyaltycookie, ":");
			if (null != loyaltyArray && loyaltyArray.length == 2) {

				String loyaltyType = loyaltyArray[0];
				String loyaltyCode = loyaltyArray[1];
				if (ICouponService.LOYALTY_TYPE_COUPON.equals(loyaltyType)) {
					LoyaltyPrefer couponLoyaltyPrefer = couponService
							.applyCoupon(email, cartItems, loyaltyCode, webCtx);
					if (couponLoyaltyPrefer.isSuccess()) {
						totalPrice = afterPrefertotal(totalPrice,
								couponLoyaltyPrefer.getValue());
						if (totalPrice >= 0) {
							prefers.add(couponLoyaltyPrefer);
						} else {
							undoCurrentPrefer();
						}
					} else {
						undoCurrentPrefer();
					}
				} else if (ICouponService.LOYALTY_TYPE_PROMO
						.equals(loyaltyType)) {
					LoyaltyPrefer promoLoyaltyPrefer = couponService
							.applyPromo(email, cartItems, loyaltyCode, webCtx);
					if (promoLoyaltyPrefer.isSuccess()) {
						totalPrice = afterPrefertotal(totalPrice,
								promoLoyaltyPrefer.getValue());
						if (totalPrice >= 0) {
							prefers.add(promoLoyaltyPrefer);
						} else {
							undoCurrentPrefer();
						}
					} else {
						undoCurrentPrefer();
					}
				} else {
					undoCurrentPrefer();
				}
			} else {
				undoCurrentPrefer();
			}
		}
		if (ctx.request().cookie(ICouponService.LOYALTY_TYPE_POINT) != null) {

			String loyaltycookie = ctx.request()
					.cookie(ICouponService.LOYALTY_TYPE_POINT).value();
			Integer costPoints = 0;
			try {
				costPoints = Integer.parseInt(loyaltycookie);
			} catch (Exception e) {
				Logger.error("loyalty point transform error");
				undoCurrentPoint();
			}
			if (costPoints > 0) {
				LoyaltyPrefer pointPrefer = pointService.applyPoints(email,
						cartItems, costPoints, webCtx);
				if (pointPrefer.isSuccess()) {
					totalPrice = afterPrefertotal(totalPrice,
							pointPrefer.getValue());
					if (totalPrice >= 0) {
						prefers.add(pointPrefer);
					} else {
						undoCurrentPoint();
					}
				} else {
					undoCurrentPoint();
				}
			} else {
				undoCurrentPoint();
			}

		}
		return prefers;
	}

	@Override
	public boolean saveAllPrefer(String email,
			List<LoyaltyPrefer> loyaltyPrefers, WebContext webCtx) {
		// 保存订单后将所有优惠清空
		Logger.debug("Clear the preferential order information,email=={}",
				email);
		this.undoAllCurrentPrefer();
		if (null != loyaltyPrefers && loyaltyPrefers.size() > 0) {
			for (int i = 0; i < loyaltyPrefers.size(); i++) {
				LoyaltyPrefer loyaltyPrefer = loyaltyPrefers.get(i);
				if (null == loyaltyPrefer) {
					Logger.error("Preferential information is empty,email=={}",
							email);
					return false;
				}
				String preferType = loyaltyPrefer.getPreferType();
				if (StringUtils.isEmpty(preferType)) {
					Logger.error(
							"Preferential information type is empty,email=={}",
							email);
					return false;
				}
				if (preferType.equals(ICouponService.LOYALTY_TYPE_COUPON)) {
					Logger.debug("Lock coupon preferential,email=={}", email);
					boolean result = couponService.saveCouponOrderPrefer(email,
							loyaltyPrefer, webCtx);
					if (!result) {
						Logger.error(
								"Save the coupon discount information failure,email=={}",
								email);
						return false;
					}
				} else if (preferType.equals(ICouponService.LOYALTY_TYPE_PROMO)) {
					Logger.debug("Lock promo preferential,email=={}", email);
					boolean result = couponService.savePromoOrderPrefer(email,
							loyaltyPrefer, webCtx);
					if (!result) {
						Logger.error(
								"Save the preferential promotion code information failure,email=={}",
								email);
						return false;
					}
				} else if (preferType.equals(ICouponService.LOYALTY_TYPE_POINT)) {
					Logger.debug("Lock integral preferential,email=={}", email);
					boolean result = pointService.saveOrderPrefer(email,
							loyaltyPrefer, webCtx);
					if (!result) {
						Logger.error(
								"Save the integral preferential information failure,email=={}",
								email);
						return false;
					}
				}
			}
			return true;
		}
		Logger.error("Currently there is no discount information,email=={}",
				email);
		return false;

	}

	public void undoCurrentPrefer() {
		Context ctx = Context.current();
		if (ctx.request().cookie(ICouponService.LOYALTY_PREFER) != null) {
			CookieUtils.removeCookie(ICouponService.LOYALTY_PREFER, ctx);
		}
	}

	public void undoCurrentPoint() {
		Context ctx = Context.current();
		if (ctx.request().cookie(ICouponService.LOYALTY_TYPE_POINT) != null) {
			CookieUtils.removeCookie(ICouponService.LOYALTY_TYPE_POINT, ctx);
		}
	}

	public void undoAllCurrentPrefer() {
		undoCurrentPrefer();
		undoCurrentPoint();
	}

	private Double afterPrefertotal(Double total, Double preferValue) {
		DoubleCalculateUtils cal = new DoubleCalculateUtils(total);
		total = cal.add(preferValue).doubleValue();
		return total;
	}

}

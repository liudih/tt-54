package services.mobile.validate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import services.ICurrencyService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.Utils;
import services.cart.ICartLifecycleService;
import services.loyalty.IPointsService;
import services.loyalty.coupon.ICouponService;
import services.member.IMemberEnquiryService;
import services.mobile.MobileService;
import services.mobile.order.CartInfoService;
import valueobjects.loyalty.Coupon;
import dto.mobile.CouponsInfo;
import entity.loyalty.IntegralUseRule;

public class ValidService {

	@Inject
	IMemberEnquiryService memberEnquiryService;
	@Inject
	ICurrencyService currencyService;
	@Inject
	ICartLifecycleService cartLifecycle;
	@Inject
	ICouponService couponService;
	@Inject
	IPointsService pointsService;
	@Inject
	CartInfoService cartInfoService;
	@Inject
	MobileService mobileService;

	/**
	 * 以email和当前站点获取规则，在将points转为当前货币相应金额
	 *
	 * @param points
	 * @param email
	 *            需确保传入参数有效，该方法中不做处理
	 * @param siteId
	 * @param curr
	 *            币种 需要转换的币种类型
	 * @param IntegralUseRule
	 *            用户使用权限类
	 * @return 保留两位有效数字
	 */
	public Double getPonintsMoney(Integer points, String email, String curr,
			IntegralUseRule rule) {
		double money = points * (rule.getFmoney() / rule.getIintegral()) * -1;
		money = currencyService.exchange(money, rule.getCcurrency(), curr);
		return new DoubleCalculateUtils(money).doubleValue();
	}

	/**
	 * 验证用户是否有可用积分
	 *
	 * @param email
	 * @param points
	 * @param siteID
	 * @param IntegralUseRule
	 *            用户使用权限类
	 * @return
	 */
	public boolean validPoints(String email, int points, Integer siteId,
			IntegralUseRule rule) {
		if (points <= getUsefulPoints(email, siteId)
				&& points <= rule.getImaxuse()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取用户权限类
	 *
	 * @param email
	 * @param siteID
	 * @return IntegralUseRule
	 */

	public IntegralUseRule getRule(String email) {
		return pointsService.getIntegralUseRule(email,
				mobileService.getWebContext());
	}

	/**
	 * 根据用户email获取用户可用积分
	 *
	 * @param email
	 * @param siteID
	 * @return
	 */
	public int getUsefulPoints(String email, int siteID) {
		return pointsService.getUsefulPoints(email, siteID);
	}

	/**
	 * 获取可用优惠劵Available Coupon
	 *
	 * @param cartId
	 * @param email
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return List<CouponUser>
	 */
	public List<CouponsInfo> getUsefulCoupon(String cartId, String email) {
		List<CouponsInfo> cuList = new ArrayList<CouponsInfo>();
		List<Coupon> coupons = couponService.getMyUsableCoupon(email, cartId,
				mobileService.getWebContext());
		coupons.forEach(c -> {
			CouponsInfo cu = new CouponsInfo();
			double amount = c.getAmount();
			if (c.isCash()) {
				cu.setFlag(1);
				cu.setDescr(c.getAmount() + " " + c.getCurrency().getCsymbol());
			} else {
				cu.setFlag(2);
				cu.setDescr(Utils.money(c.getPercent()) + "% OFF");
			}
			cu.setDis(amount);
			cu.setMinAmt(0D);
			cu.setVdate(0);
			cuList.add(cu);
		});
		return cuList;
	}

	/**
	 * 应用优惠劵Available Coupon
	 *
	 * @param cartId
	 * @param email
	 * @param code
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean applyCoupon(String cartId, String email, String code) {
		return couponService.applyCoupon(email, cartId, code,
				mobileService.getWebContext());
	}

	/**
	 * 删除订单优惠劵
	 *
	 * @param cartId
	 * @param email
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean deleteCoupon(String cartId, String code) {
		return couponService.delCartCoupon(cartId, code);
	}

	/**
	 * 删除订单使用积分
	 *
	 * @param email
	 * @param ltc
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean delPoints(String cardid) {
		return pointsService.cancelUsedPoint(cardid);
	}

	/**
	 * 删除使用的推广码(未实现controllers)
	 *
	 * @param email
	 * @param ltc
	 * @param userCurrency
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	public boolean delSpcode(String email, String ltc, String userCurrency,
			int siteID, int languageID) {
		// Cart cart = cartInfoService.getCurrentCart(mobileService.getUUID(),
		// true);
		// if (cart != null && cart.delExtraLine(couponExtrasProvider.getId()))
		// {
		// return true;
		// }
		return false;
	}

	public boolean usePoint(String cartId, int point) {
		return pointsService.usePoint(point, cartId,
				mobileService.getWebContext());
	}
}

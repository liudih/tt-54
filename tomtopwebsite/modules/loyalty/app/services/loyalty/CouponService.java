package services.loyalty;


import javax.inject.Inject;

import mapper.loyalty.CouponBaseMapper;
import play.Logger;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import valueobjects.order_api.ParValue;

public class CouponService {

	@Inject
	FoundationService foundationService;

	@Inject
	CurrencyService currencyService;

	@Inject
	CouponBaseMapper couponBaseMapper;

	/**
	 * 状态：可用
	 */
	public static int STATUS_AVAILABLE = 0;

	/**
	 * 状态：被锁定
	 */
	public static int STATUS_LOCKED = 1;

	/**
	 * 状态：已被使用
	 */
	public static int STATUS_BE_USED = 2;

	/**
	 * 根据code查询该购物卷面值
	 *
	 * @param code
	 * @return 转为当前站点默认货币，保留两位小数，若未查到该购物卷，则返回0.0
	 * @author luojiaheng
	 */
	public Double getMoney(String code) {
		ParValue parValue = couponBaseMapper.getParValue(code);
		if (null == parValue) {
			Logger.info(
					"CouponService getMoney() not find coupon, parValue is null, return 0, code: {}",
					code);
			return 0.0;
		}
		Double rate = currencyService.exchange(parValue.getValue(),
				parValue.getCurrency(), foundationService.getCurrency());
		return new DoubleCalculateUtils(rate).doubleValue();
	}

	/**
	 * 验证优惠卷是否可用
	 *
	 * @param code
	 * @return
	 * @author luojiaheng
	 */
	public boolean validCoupon(String code, Integer websiteId) {
		int i = couponBaseMapper.valid(code);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public boolean setStatusAvailable(String code) {
		return setStatus(code, STATUS_AVAILABLE);
	}

	public boolean setStatusLocked(String code) {
		return setStatus(code, STATUS_LOCKED);
	}

	public boolean setStatusBeUsed(String code) {
		return setStatus(code, STATUS_BE_USED);
	}

	private boolean setStatus(String code, Integer state) {
		int i = couponBaseMapper.updateStatus(code, state);
		if (1 == i) {
			return true;
		}
		return false;
	}

}

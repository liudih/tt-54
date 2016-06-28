package services.loyalty;

import java.util.List;

import valueobjects.loyalty.LoyaltyPrefer;

public interface ILoyaltyService {

	public static final String LOYALTY_PREFER = "loyalty";
	public static final String LOYALTY_TYPE_PROMO = "promo";
	public static final String LOYALTY_TYPE_COUPON = "coupon";
	public static final String LOYALTY_TYPE_POINT = "point";

	public abstract LoyaltyPrefer applyCoupon(String email, String code);

	public abstract LoyaltyPrefer applyPromo(String email, String code);

	public abstract LoyaltyPrefer getCurrentPrefer(String email);

	/**
	 * 取消应用优惠
	 * 
	 * @param email
	 * @param code
	 * @return
	 */
	public abstract void undoCurrentPrefer();

	public abstract void undoCurrentPoint();

	public abstract LoyaltyPrefer applyPoints(String email, Integer costpoints);

	public abstract List<LoyaltyPrefer> getAllCurrentPrefer();

}
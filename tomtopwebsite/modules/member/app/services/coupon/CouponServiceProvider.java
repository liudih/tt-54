package services.coupon;

/**
 * 该类是为了解决相互依赖而建
 * @author lijun
 *
 */
public interface CouponServiceProvider {

	/**
	 * 为新注册用户赠送优惠券
	 * @param userEmail 用户email
	 */
	public void giftCouponForSignin(String userEmail);
	
}

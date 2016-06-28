package services.loyalty.coupon;

import java.util.List;

import context.WebContext;
import valueobjects.base.Page;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyCoupon;
import valueobjects.loyalty.LoyaltyPrefer;
import entity.loyalty.Coupon;

/**
 * 
 * @author lijun
 *
 */
public interface ICouponService {
	
	public static final String LOYALTY_PREFER = "loyalty";
	public static final String LOYALTY_TYPE_PROMO = "promo";
	public static final String LOYALTY_TYPE_COUPON = "coupon";
	public static final String LOYALTY_TYPE_POINT = "point";

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page<Coupon> selectForPage(int page, int pageSize, String email,
			String code);

	/**
	 * 查询数据总条数
	 * 
	 * @return
	 */
	public int getTotal();

	/**
	 * 通过id查询操作
	 * 
	 * @param paras
	 * @return
	 */
	public Coupon selectById(int id);

	/**
	 * 为新注册用户赠送优惠券
	 * 
	 * @param userEmail
	 *            用户email
	 * @return codes
	 */
	public List<Coupon> giftCouponForSignin(String userEmail, int siteId);

	/**
	 * 用户订阅时赠送优惠券
	 * 
	 * @param userEmail
	 * @return codes
	 */
	public List<Coupon> giftCouponForRSS(String userEmail, int siteId);

	/**
	 * 获取我的优惠券信息
	 * 
	 * @param page
	 * @param pageSize
	 * @param userEmail
	 * @return
	 */
	public Page<Coupon> selectMyCouponForPage(int page, int pageSize,
			String userEmail);

	/**
	 * 该用户是否已经获取过订阅优惠券
	 * 
	 * @param userEmail
	 * @return
	 */
	public boolean isExistedForRSS(String userEmail);

	/**
	 * 获取我的未使用优惠券总数
	 * 
	 * @param paras
	 * @return
	 */
	public int getTotalMyCouponUnused(String userEmail);

	/**
	 * 获取未使用的优惠券
	 * 
	 * @param page
	 * @param pageSize
	 * @param userEmail
	 * @return
	 */
	public Page<Coupon> selectMyCouponUnusedForPage(int page, int pageSize,
			String userEmail);

	/**
	 * 获取我的已经使用的优惠券
	 * 
	 * @param page
	 * @param pageSize
	 * @param userEmail
	 * @return
	 */
	public Page<Coupon> selectMyCouponUsedForPage(int page, int pageSize,
			String userEmail);

	/**
	 * 该用户是否已经获取过注册优惠券
	 * 
	 * @param userEmail
	 * @return
	 */
	public boolean isExistedForSignin(String userEmail);

	/**
	 * 获取我的为使用的优惠券
	 * 
	 * @param userEmail
	 * @return
	 */
	public List<Coupon> selectMyCouponUnused(String userEmail);

	/**
	 * 判断我的优惠券是否可用
	 * 
	 * @param userEmail
	 * @param code
	 * @param siteId
	 * @return
	 */
	public boolean myCouponUseable(String userEmail, String code);

	/**
	 * 应用优惠券
	 * 
	 * @return true:应用成功,false:应用失败
	 * @author xiaoch
	 */
	public boolean applyCoupon(String email, String cartId, String code,
			WebContext webContext);

	/**
	 * 从购物车移除优惠券
	 * 
	 * @return true:成功,false:失败
	 * 
	 * @author xiaoch
	 */
	public boolean delCartCoupon(String cartId, String code);

	/**
	 * 为新注册用户补发优惠券
	 * 
	 * @param userEmail
	 *            用户email
	 * @return codes
	 */
	public List<Coupon> giftCouponForSigninRessiue(String userEmail, int siteId);

	/**
	 * 用户订阅时补发优惠券
	 * 
	 * @param userEmail
	 * @return codes
	 */
	public List<Coupon> giftCouponForRSSRessiue(String userEmail, int siteId);

	/**
	 * 获取可用的优惠券
	 * 
	 * author lijun
	 */
	public List<valueobjects.loyalty.Coupon> getMyUsableCoupon(String email,
			String cartId, WebContext webCtx);

	/**
	 * 应用优惠券
	 * 
	 * @return 应用优惠券后的优惠信息
	 * @author xiaoch
	 */
	public LoyaltyPrefer applyCoupon(String email, List<CartItem> cartItems,
			String code, WebContext webContext);

	/**
	 * 应用推广码
	 * 
	 * @return 应用推广码后的优惠信息
	 * @author xiaoch
	 */
	public LoyaltyPrefer applyPromo(String email, List<CartItem> cartItems,
			String code, WebContext webContext);

	/**
	 * 应用优惠券
	 * 
	 * @return 应用优惠券后的优惠信息
	 * @author xiaoch
	 */
	public LoyaltyPrefer applyCouponToDB(String email,
			List<CartItem> cartItems, String code, WebContext webContext);

	/**
	 * 应用推广码
	 * 
	 * @return 应用推广码后的优惠信息
	 * @author xiaoch
	 */
	public LoyaltyPrefer applyPromoToDB(String email, List<CartItem> cartItems,
			String code, WebContext webContext);

	/**
	 * 获取当前购物车优惠信息
	 * 
	 * @return 如果已经使用了优惠信息,会再次验证当前的购物车商品是否满足规则
	 * @author xiaoch
	 */
	public LoyaltyPrefer getCurrentPrefer(String email,
			List<CartItem> cartItems, WebContext webContext);

	/**
	 * 查找用户可用的优惠券
	 * 
	 * @param email
	 * @param cartItems
	 * @param webCtx
	 * @return
	 */
	public List<LoyaltyCoupon> getMyUsableCoupon(String email,
			List<CartItem> cartItems, WebContext webCtx);

	/**
	 * 将成功应用的coupon设置为已用
	 * @param email
	 * @param loyaltyPrefer
	 * @param webCtx
	 * @return
	 */
	public boolean saveCouponOrderPrefer(String email,
			LoyaltyPrefer loyaltyPrefer, WebContext webCtx);
	
	/**
	 * 将成功应用的promo设置为已用
	 * @param email
	 * @param loyaltyPrefer
	 * @param webCtx
	 * @return
	 */
	public boolean savePromoOrderPrefer(String email,
			LoyaltyPrefer loyaltyPrefer, WebContext webCtx);
	/**
	 * 第三方根据规则给某人生成优惠券
	 * @param email
	 * @param ruleId
	 * @param webCtx
	 * @return 如果返回为空则生成失败
	 */
	public Coupon createCouponByRule(String email, Integer ruleId,
			WebContext webCtx);
	
}

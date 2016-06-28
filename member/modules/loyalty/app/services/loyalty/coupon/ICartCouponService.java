package services.loyalty.coupon;

import entity.loyalty.CartCoupon;

/**
 * 购物车-优惠券关联关系调用接口
 * 
 * @author xiaoch
 *
 */
public interface ICartCouponService {

	boolean add(CartCoupon cartCoupon);

	boolean update(CartCoupon cartCoupon);

	boolean delete(Integer id);

	CartCoupon get(Integer id);

	/**
	 * 设置指定购物车的购物券状态为已用
	 * 
	 * @param cartId
	 * @param code
	 * @return
	 */
	boolean setStatusBeUsed(String cartId, String code);
	
	CartCoupon getCartCouponByCondition(CartCoupon cartCoupon);
	
	boolean updateOrderCoupon(CartCoupon cartCoupon);
	
	boolean addOrderCoupon(CartCoupon cartCoupon);
	
	CartCoupon getCartCouponByCode(String code);
		
}

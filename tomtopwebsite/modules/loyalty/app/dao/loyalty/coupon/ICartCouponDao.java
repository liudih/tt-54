package dao.loyalty.coupon;

import entity.loyalty.CartCoupon;

public interface ICartCouponDao {

	public int insert(CartCoupon cartCoupon);

	public int delete(Integer id);

	public int update(CartCoupon cartCoupon);

	public CartCoupon get(Integer id);

	public CartCoupon getCartCouponByCondition(CartCoupon cartCoupon);

	public int updateOrderCoupon(CartCoupon cartCoupon);

	public int insertOrderCoupon(CartCoupon cartCoupon);
	
	public CartCoupon getCartCouponByCode(String code);

}

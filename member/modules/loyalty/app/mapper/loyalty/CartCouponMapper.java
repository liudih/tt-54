package mapper.loyalty;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.CartCoupon;

public interface CartCouponMapper {

	int insert(CartCoupon cartCoupon);

	int delete(Integer id);

	int update(CartCoupon cartCoupon);

	@Select("select * from t_cart_coupon where iid=#{0}")
	CartCoupon get(Integer id);

	CartCoupon getCartCouponByCondition(CartCoupon cartCoupon);

	/**
	 * 新购物车流程优惠券使用成功后的保存方法
	 * 
	 * @param cartCoupon
	 * @return
	 */
	int updateOrderCoupon(CartCoupon cartCoupon);

	int insertCartCoupon(CartCoupon cartCoupon);

	@Select("select iid,ccode,istatus,ccartid,cemail,dusedate,iorderid,cordernumber as orderNumber from t_cart_coupon where ccode=#{0}")
	CartCoupon getCartCouponByCode(String code);
}

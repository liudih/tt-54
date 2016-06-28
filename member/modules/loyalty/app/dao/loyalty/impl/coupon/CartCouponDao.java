package dao.loyalty.impl.coupon;

import javax.inject.Inject;

import mapper.loyalty.CartCouponMapper;
import dao.loyalty.coupon.ICartCouponDao;
import entity.loyalty.CartCoupon;

public class CartCouponDao implements ICartCouponDao {

	@Inject
	CartCouponMapper cartCouponMapper;

	@Override
	public int insert(CartCoupon cartCoupon) {
		return cartCouponMapper.insert(cartCoupon);
	}

	@Override
	public int delete(Integer id) {
		return cartCouponMapper.delete(id);
	}

	@Override
	public int update(CartCoupon cartCoupon) {
		return cartCouponMapper.update(cartCoupon);
	}

	@Override
	public CartCoupon get(Integer id) {
		// TODO Auto-generated method stub
		return cartCouponMapper.get(id);
	}

	@Override
	public CartCoupon getCartCouponByCondition(CartCoupon cartCoupon) {
		return cartCouponMapper.getCartCouponByCondition(cartCoupon);
	}

	@Override
	public int updateOrderCoupon(CartCoupon cartCoupon) {
		return cartCouponMapper.updateOrderCoupon(cartCoupon);
	}

	@Override
	public int insertOrderCoupon(CartCoupon cartCoupon) {
		return cartCouponMapper.insertCartCoupon(cartCoupon);
	}

	@Override
	public CartCoupon getCartCouponByCode(String code) {
		return cartCouponMapper.getCartCouponByCode(code);
	}

}

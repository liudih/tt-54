package services.loyalty.coupon.impl;

import javax.inject.Inject;

import dao.loyalty.coupon.ICartCouponDao;
import entity.loyalty.CartCoupon;
import enums.loyalty.coupon.manager.CartCouponBack;
import services.loyalty.coupon.ICartCouponService;

/**
 * 
 * @author xiaoch
 *
 */
public class CartCouponService implements ICartCouponService {

	@Inject
	ICartCouponDao cartCouponDao;

	@Override
	public boolean add(CartCoupon cartCoupon) {
		int result = cartCouponDao.insert(cartCoupon);
		return result > 0 ? true : false;
	}

	@Override
	public boolean update(CartCoupon cartCoupon) {
		int result = cartCouponDao.update(cartCoupon);
		return result > 0 ? true : false;
	}

	@Override
	public boolean delete(Integer id) {
		int result = cartCouponDao.delete(id);
		return result > 0 ? true : false;
	}

	@Override
	public CartCoupon get(Integer id) {
		return cartCouponDao.get(id);
	}

	@Override
	public boolean setStatusBeUsed(String cartId, String code) {
		CartCoupon cartCoupon = new CartCoupon();
		cartCoupon.setCcartid(cartId);
		cartCoupon.setCcode(code);
		CartCoupon queryCartCoupon = cartCouponDao
				.getCartCouponByCondition(cartCoupon);
		if (null != queryCartCoupon) {
			cartCoupon.setIstatus(CartCouponBack.UseStatus.USED.getStatusid());
			int result = cartCouponDao.update(cartCoupon);
			return result > 0 ? true : false;
		}
		return false;
	}

	@Override
	public CartCoupon getCartCouponByCondition(CartCoupon cartCoupon) {
		return cartCouponDao.getCartCouponByCondition(cartCoupon);
	}

	@Override
	public boolean updateOrderCoupon(CartCoupon cartCoupon) {
		int result = cartCouponDao.updateOrderCoupon(cartCoupon);
		return result > 0 ? true : false;
	}

	@Override
	public boolean addOrderCoupon(CartCoupon cartCoupon) {
		int result = cartCouponDao.insertOrderCoupon(cartCoupon);
		return result > 0 ? true : false;
	}

	@Override
	public CartCoupon getCartCouponByCode(String code) {
		return cartCouponDao.getCartCouponByCode(code);
	}



}

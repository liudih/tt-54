package services.loyalty;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import entity.loyalty.OrderCoupon;
import forms.loyalty.OrderCouponForm;
import mapper.loyalty.OrderCouponMapper;

/**
 * 订单优惠券服务类
 * 
 * @author guozy
 *
 */
public class OrderCouponService {

	@Inject
	private OrderCouponMapper orderCouponMapper;

	/**
	 * 查询所有的信息
	 * 
	 * @return
	 */
	public List<OrderCoupon> getOrderCoupons() {
		return orderCouponMapper.getOrderCoupons();
	};

	/**
	 * 通过优惠券号码获取OrderCoupon
	 * 
	 * @param ccode
	 * @return
	 */
	public OrderCoupon getOrderCouponByCcode(String ccode) {
		return orderCouponMapper.getOrderCouponsByCcode(ccode);
	};

	/**
	 * 根据状态、订单号获取优惠券号码
	 * 
	 * @param orderCoupon
	 * @return
	 */
	public List<OrderCoupon> getCCodeByStatusAndOrderNumAndDate(
			OrderCouponForm orderCoupon) {
		return orderCouponMapper.getCCodeByStatusAndOrderNumAndDate(
				orderCoupon.getStartDate(),
				orderCoupon.getEndDate(),
				orderCoupon.getIstatus(), orderCoupon.getCordernumber());
	}
}

package handlers.loyalty;

import javax.inject.Inject;

import mapper.loyalty.OrderCouponMapper;
import mapper.loyalty.OrderPointsMapper;
import services.loyalty.CouponService;
import services.loyalty.IPointsService;

import com.google.common.eventbus.Subscribe;

import events.order.PaymentConfirmationEvent;

public class OrderExtrasHandle {
	@Inject
	IPointsService pointsService;

	@Inject
	CouponService couponService;

	@Inject
	OrderPointsMapper orderPointsMapper;

	@Inject
	OrderCouponMapper orderCouponMapper;

	@Subscribe
	public void onPaySuccess(PaymentConfirmationEvent event) {
		if (event.getOrderValue().getOrder().getFextra() == 0) {
			return;
		}
		Integer orderId = event.getOrderValue().getOrder().getIid();
		Integer pointsId = orderPointsMapper.getPointsIdByOrderId(orderId);
		if (pointsId != null) {
			pointsService.unlockPointsWithType("cost", pointsId);
		}
		String coupon = orderCouponMapper.getCouponByOrderId(orderId);
		if (coupon != null) {
			couponService.setStatusBeUsed(coupon);
		}
	}
}

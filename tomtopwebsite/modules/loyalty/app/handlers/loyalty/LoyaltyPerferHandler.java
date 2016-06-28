package handlers.loyalty;

import javax.inject.Inject;

import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponService;

import com.google.common.eventbus.Subscribe;

import entity.loyalty.CartCoupon;
import events.loyalty.LoyaltyPreferEvent;

public class LoyaltyPerferHandler {
	
	@Inject
	ICartCouponService ccService;
	
	@Subscribe
	public void orderCreae(LoyaltyPreferEvent event) {
		if (null!=event.getPreferType()){
			String preferType=event.getPreferType();
			switch (preferType) {
			case ICouponService.LOYALTY_TYPE_COUPON:{
				saveCouponInfo(event);
				break;}
			case ICouponService.LOYALTY_TYPE_POINT:
			{
				break;
			}
			default:
				break;
			}
		}
	}
	
	private void saveCouponInfo(LoyaltyPreferEvent event){
		String code = event.getCode();
		if(code!=null){
			CartCoupon cartCoupon = new CartCoupon();
			//int orderId = order.getIid();
			String orderNumber = event.getOrderNum();
			cartCoupon.setCcode(code);
			cartCoupon.setOrderId(event.getOrderId());
			cartCoupon.setOrderNumber(orderNumber);
			ccService.update(cartCoupon);
		}
		
	}
	private void savePointInfo(){}
}

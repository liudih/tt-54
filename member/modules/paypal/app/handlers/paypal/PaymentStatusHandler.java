package handlers.paypal;

import mapper.order.OrderMapper;
import play.Logger;
import services.order.IOrderStatusService;
import services.payment.PaymentConfirmationService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import dto.order.Order;
import events.paypal.PaymentEvent;

/**
 * 
 * @author lijun
 *
 */
public class PaymentStatusHandler {

	@Inject
	IOrderStatusService service;

	@Inject
	OrderMapper orderMapper;
	
	@Inject
	PaymentConfirmationService paymentEnquiry;

	@Subscribe
	public void changeOrderStatus(PaymentEvent event) {
		String orderNum = event.getOrderNum();
		Order order = orderMapper.getOrderByOrderNumber(orderNum);
		if (order == null) {
			Logger.debug("can not find this order:{} when change order status",
					orderNum);
			return;
		}
		String status = event.getStatus();
		//修改支付状态
		if(IOrderStatusService.PAYMENT_PENDING.equals(status)){
			this.service.changeOrdePaymentStatus(orderNum, 2);
		}else if(IOrderStatusService.PAYMENT_CONFIRMED.equals(status)){
			this.service.changeOrdePaymentStatus(orderNum, 1);
		}
		
		double total = order.getFgrandtotal();
//		String totalStr = Utils.money(total, order.getCcurrency());
//		total = Double.parseDouble(totalStr);
		// 实付金额
		double amt = Double.parseDouble(event.getAmt());

		
		Integer state = service.getIdByName(status);
		if (state == null) {
			Logger.debug(
					"ignore change order:{} status,because status parameter is null",
					orderNum);
			return;
		}

//		if (total != amt) {
//			Logger.debug(
//					"ignore change order:{} status,because the amount actually paid is less than the total amount",
//					orderNum);
//			// 把状态改成pending
//			state = service.getIdByName(IOrderStatusService.PAYMENT_PENDING);
//		}
		
		String transactionId = event.getTransactionId();
		String receiverAccount = event.getReceiverAccount();
		String paymentId = event.getPaymentId();

		boolean result = service.changeOrdeStatusAndTransactionInfo(
				order.getIid(), state, transactionId, receiverAccount,paymentId);
		
		if (result) {
			Logger.debug("order:{} change status:{} succeed", orderNum, status);
		} else {
			Logger.debug("order:{} change status:{} failed", orderNum, status);
		}
	}
}

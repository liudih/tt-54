package services.payment;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import services.IWebsiteService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.IOrderUpdateService;
import valueobjects.order_api.OrderValue;

import com.google.common.eventbus.EventBus;

import dto.order.Order;
import dto.order.OrderDetail;
import events.order.PaymentConfirmationEvent;

public class PaymentConfirmationService implements IPaymentConfirmationService {

	@Inject
	private IOrderEnquiryService enquiryService;
	@Inject
	private IOrderStatusService statusSerice;
	@Inject
	private IOrderUpdateService updateService;
	@Inject
	private EventBus eventBus;
	@Inject
	private IWebsiteService websiteService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.payment.IPaymentConfirmationService#confirmPayment(java.lang
	 * .String, java.lang.String, int)
	 */
	@Override
	public boolean confirmPayment(String orderId, String transactionId,
			int langId) {
		Order order = enquiryService.getOrderById(orderId);
		if (null == order) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}",
					orderId, transactionId);
			return false;
		}
		List<OrderDetail> details = enquiryService.getOrderDetails(orderId);

		statusSerice.changeOrdeStatus(order.getIid(),
				IOrderStatusService.PAYMENT_CONFIRMED);

		statusSerice.changeOrdePaymentStatus(order.getCordernumber(), 1);

		updateService.updateTransactionId(order.getIid(), transactionId);
		eventBus.post(new PaymentConfirmationEvent(new OrderValue(order,
				details), transactionId, langId));
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.payment.IPaymentConfirmationService#confirmPayment(java.lang
	 * .Integer, java.lang.String)
	 */
	@Override
	public boolean confirmPayment(Integer orderId, String transactionId) {
		Order order = enquiryService.getOrderById(orderId);
		List<OrderDetail> details = enquiryService.getOrderDetails(orderId);
		if (null == order) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}",
					orderId, transactionId);
			return false;
		}
		statusSerice.changeOrdeStatus(order.getIid(),
				IOrderStatusService.PAYMENT_CONFIRMED);
		updateService.updateTransactionId(order.getIid(), transactionId);
		Integer webisteId = order.getIwebsiteid();
		Integer ilanguageid = websiteService.getWebsite(webisteId)
				.getIlanguageid();
		eventBus.post(new PaymentConfirmationEvent(new OrderValue(order,
				details), transactionId, ilanguageid));
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.payment.IPaymentConfirmationService#confirmPayment(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean confirmPayment(String orderId, String transactionId,
			String status) {
		boolean flag = statusSerice.changeOrdeStatus(orderId, status);
		if (!flag) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}, status: {}",
					orderId, transactionId, status);
			return false;
		}
		updateService.updateTransactionId(orderId, transactionId);
		return true;
	}

	@Override
	public boolean confirmPayment(String orderNumber, String transactionId) {
		Order order = enquiryService.getOrderById(orderNumber);
		List<OrderDetail> details = enquiryService.getOrderDetails(orderNumber);
		if (null == order) {
			Logger.info(
					"order can not found in confirmPayment, orderID: {}, transactionID: {}",
					orderNumber, transactionId);
			return false;
		}
		statusSerice.changeOrdeStatus(order.getIid(),
				IOrderStatusService.PAYMENT_CONFIRMED);
		updateService.updateTransactionId(order.getIid(), transactionId);
		Integer webisteId = order.getIwebsiteid();
		Integer ilanguageid = websiteService.getWebsite(webisteId)
				.getIlanguageid();
		eventBus.post(new PaymentConfirmationEvent(new OrderValue(order,
				details), transactionId, ilanguageid));
		return true;
	}
}

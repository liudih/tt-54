package services.paypal;

import services.order.IOrderStatusService;
import valueobjects.paypal_api.IPaypalPaymentStatus;

/**
 * 订单付款状态转换器(把状态转换成t_order_status表中的状态)
 * 
 * @author lijun
 *
 */
public class StatusTransformer {

	/**
	 * 把paypal返回的付款状态转换成tomtop中定义的付款状态
	 * 
	 * @param status
	 * @return
	 */
	public static String transformPaypal(String status) {
		String result = null;

		switch (status) {
		case IPaypalPaymentStatus.COMPLETED:
		case IPaypalPaymentStatus.PROCESSED:
		case IPaypalPaymentStatus.CANCELED_REVERSAL:
			result = IOrderStatusService.PAYMENT_CONFIRMED;
			break;
		case IPaypalPaymentStatus.PENDING:
			result = IOrderStatusService.PAYMENT_PENDING;
			break;
		case IPaypalPaymentStatus.PROGRESSING:
			result = IOrderStatusService.PAYMENT_PROCESSING;
			break;
		default:
			result = IOrderStatusService.PAYMENT_PENDING;
			break;
		}

		return result;
	}
}

package handlers.paypal;

import play.Logger;
import services.paypal.PaymentServices;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import entity.payment.PaypaiReturnLog;

/**
 * 插入paypal操作日志到数据库
 * 
 * @author lijun
 *
 */
public class PaypalPaymentLogHandler {

	@Inject
	PaymentServices services;

	/**
	 * 记录paypal支付过程中的信息
	 */
	@Subscribe
	public void doLog(PaypaiReturnLog event) {
//		if(IPaypalPaymentStatus.class.isAssignableFrom(event.getClass())){
//			Logger.debug("start to do log for paypal");
//			IPaypalPaymentStatus status = (IPaypalPaymentStatus) event;
//			Map<String, String> reply = status.getPaypalReply();
//			ObjectMapper objectMapper = new ObjectMapper();
//			JSONObject json = objectMapper.convertValue(reply, JSONObject.class);
//			Logger.debug("log:{}",json.toString());
//			services.InsertLog(status.getWebSiteId(), status.getOrderNum(), json.toString());
//		}
		Logger.debug("start to do log for paypal");
		services.InsertLog(event.getIwebsiteid(), event.getCorderid(), event.getCcontent(),event.getCtransactionid());
	}
}

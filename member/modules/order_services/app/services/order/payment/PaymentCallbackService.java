package services.order.payment;

import java.util.List;

import javax.inject.Inject;

import dao.order.IPaymentCallbackEnquiryDao;
import dao.order.IPaymentCallbackUpdateDao;
import dto.order.PaymentCallback;

public class PaymentCallbackService implements IPaymentCallbackService {
	@Inject
	private IPaymentCallbackEnquiryDao enquiryDao;
	@Inject
	private IPaymentCallbackUpdateDao updateDao;

	/* (non-Javadoc)
	 * @see services.order.payment.IPaymentCallbackService#insert(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean insert(String orderNumber, String content, String paymentID,
			String response, Integer siteID) {
		PaymentCallback pc = new PaymentCallback();
		pc.setCcontent(content);
		pc.setCordernumber(orderNumber);
		pc.setCpaymentid(paymentID);
		pc.setCresponse(response);
		pc.setIwebsiteid(siteID);
		return insert(pc);
	}

	/* (non-Javadoc)
	 * @see services.order.payment.IPaymentCallbackService#insert(dto.order.PaymentCallback)
	 */
	@Override
	public boolean insert(PaymentCallback pc) {
		int i = updateDao.insert(pc);
		return i == 1 ? true : false;
	}

	/* (non-Javadoc)
	 * @see services.order.payment.IPaymentCallbackService#getByOrderNumerAndSiteID(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<PaymentCallback> getByOrderNumerAndSiteID(String orderNumber,
			Integer siteID) {
		return enquiryDao.getByOrderNumberAndSiteID(orderNumber, siteID);
	}
}

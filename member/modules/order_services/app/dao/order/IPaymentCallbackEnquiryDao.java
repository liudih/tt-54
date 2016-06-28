package dao.order;

import java.util.List;

import dao.IOrderEnquiryDao;
import dto.order.PaymentCallback;

public interface IPaymentCallbackEnquiryDao extends IOrderEnquiryDao {
	List<PaymentCallback> getByOrderNumberAndSiteID(String orderNumber,
			Integer site);
}

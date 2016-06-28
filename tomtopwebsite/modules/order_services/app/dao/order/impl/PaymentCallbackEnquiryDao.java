package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.PaymentCallbackMapper;
import dao.order.IPaymentCallbackEnquiryDao;
import dto.order.PaymentCallback;

public class PaymentCallbackEnquiryDao implements IPaymentCallbackEnquiryDao {
	@Inject
	PaymentCallbackMapper mapper;

	@Override
	public List<PaymentCallback> getByOrderNumberAndSiteID(String orderNumber,
			Integer site) {
		return mapper.getByOrderNumberAndSiteID(orderNumber, site);
	}

}

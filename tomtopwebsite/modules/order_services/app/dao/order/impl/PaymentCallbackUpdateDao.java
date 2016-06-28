package dao.order.impl;

import javax.inject.Inject;

import mapper.order.PaymentCallbackMapper;
import dao.order.IPaymentCallbackUpdateDao;
import dto.order.PaymentCallback;

public class PaymentCallbackUpdateDao implements IPaymentCallbackUpdateDao {
	@Inject
	PaymentCallbackMapper mapper;

	@Override
	public int insert(PaymentCallback pc) {
		return mapper.insert(pc);
	}

}

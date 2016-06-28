package services.order;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dto.order.OrderPayment;
import mapper.order.OrderPaymentMapper;
import play.data.DynamicForm;
import services.base.utils.JsonFormatUtils;
import services.base.utils.StringUtils;

public class OrderPaymentService {
	@Inject
	OrderPaymentMapper paymentMapper;

	public boolean saveOrUpdate(OrderPayment op) {
		int i = paymentMapper.update(op);
		if (i != 1) {
			paymentMapper.insert(op);
		}
		return true;
	}

	public boolean createOrderPayment(String orderId, String paymentId,
			DynamicForm df) {
		OrderPayment op = new OrderPayment();
		op.setCjson(JsonFormatUtils.beanToJson(df.data()));
		op.setCorderid(orderId);
		op.setCpaymentid(paymentId);
		return saveOrUpdate(op);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getForm(String orderId, String paymentId) {
		OrderPayment op = paymentMapper.selectByOrderId(orderId, paymentId);
		if (null != op && StringUtils.notEmpty(op.getCjson())) {
			return JsonFormatUtils.jsonToBean(op.getCjson(), HashMap.class);
		}
		return null;
	}
}

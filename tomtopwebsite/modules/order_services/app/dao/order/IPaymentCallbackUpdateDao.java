package dao.order;

import dao.IOrderUpdateDao;
import dto.order.PaymentCallback;

public interface IPaymentCallbackUpdateDao extends IOrderUpdateDao {
	int insert(PaymentCallback pc);
}

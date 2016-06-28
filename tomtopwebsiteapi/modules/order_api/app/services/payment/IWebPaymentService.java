package services.payment;

import dto.payment.AbstractPaymentParam;
import dto.payment.WebPaymentResult;

public interface IWebPaymentService {

	public abstract WebPaymentResult pay(AbstractPaymentParam param);

}
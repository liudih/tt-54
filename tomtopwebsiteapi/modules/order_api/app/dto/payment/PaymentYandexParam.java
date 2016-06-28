package dto.payment;

public class PaymentYandexParam extends AbstractPaymentParam {
	public PaymentYandexParam(String orderNumber, int langID) {
		super(orderNumber, langID);
	}

	@Override
	public String getPaymentID() {
		return "oceanpayment_yandex";
	}

}

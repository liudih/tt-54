package dto.payment;

public class PaymentPaypalParam extends AbstractPaymentParam {
	private static final long serialVersionUID = 1L;

	public PaymentPaypalParam(String orderNumber, int langID) {
		super(orderNumber, langID);
	}

	@Override
	public String getPaymentID() {
		return "paypal";
	}

}

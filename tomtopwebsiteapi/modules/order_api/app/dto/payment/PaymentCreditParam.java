package dto.payment;

public class PaymentCreditParam extends AbstractPaymentParam {
	private String billAddressId;

	public PaymentCreditParam(String orderNumber, int langID) {
		super(orderNumber, langID);
	}

	public String getBillAddressId() {
		return billAddressId;
	}

	public void setBillAddressId(String billAddressId) {
		this.billAddressId = billAddressId;
	}

	@Override
	public String getPaymentID() {
		return "oceanpayment_credit";
	}

}

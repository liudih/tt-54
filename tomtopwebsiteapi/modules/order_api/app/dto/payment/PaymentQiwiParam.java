package dto.payment;

public class PaymentQiwiParam extends AbstractPaymentParam {
	private String qiwiAccount;
	private String qiwiCountry;

	public PaymentQiwiParam(String orderNumber, int langID) {
		super(orderNumber, langID);
	}

	public String getQiwiAccount() {
		return qiwiAccount;
	}

	public void setQiwiAccount(String qiwiAccount) {
		this.qiwiAccount = qiwiAccount;
	}

	public String getQiwiCountry() {
		return qiwiCountry;
	}

	public void setQiwiCountry(String qiwiCountry) {
		this.qiwiCountry = qiwiCountry;
	}

	@Override
	public String getPaymentID() {
		return "oceanpayment_qiwi";
	}

}

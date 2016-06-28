package dto.payment;

public class PaymentBoletoParam extends AbstractPaymentParam {

	private String userName;
	private String userEmail;
	private String pay_typeCode;
	private String pay_cpf;

	public PaymentBoletoParam(String orderNumber, int langID) {
		super(orderNumber, langID);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPay_typeCode() {
		return pay_typeCode;
	}

	public void setPay_typeCode(String pay_typeCode) {
		this.pay_typeCode = pay_typeCode;
	}

	public String getPay_cpf() {
		return pay_cpf;
	}

	public void setPay_cpf(String pay_cpf) {
		this.pay_cpf = pay_cpf;
	}

	@Override
	public String getPaymentID() {
		return "oceanpayment_boleto";
	}

}

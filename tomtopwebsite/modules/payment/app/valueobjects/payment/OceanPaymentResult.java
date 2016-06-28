package valueobjects.payment;

import play.data.validation.Constraints.Required;

public class OceanPaymentResult {

	@Required
	private String response_type;
	@Required
	private String order_number;
	@Required
	private String account;
	@Required
	private String terminal;
	@Required
	private String signValue;
	@Required
	private String payment_status;
	@Required
	private String order_currency;
	@Required
	private String order_amount;
	private String card_number;
	@Required
	private String payment_id;
	@Required
	private String payment_authType;
	@Required
	private String payment_details;
	private String payment_risk;
	private String order_notes;
	private String methods;
	private String payment_country;

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getSignValue() {
		return signValue;
	}

	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}

	public String getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}

	public String getOrder_currency() {
		return order_currency;
	}

	public void setOrder_currency(String order_currency) {
		this.order_currency = order_currency;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

	public String getPayment_authType() {
		return payment_authType;
	}

	public void setPayment_authType(String payment_authType) {
		this.payment_authType = payment_authType;
	}

	public String getPayment_details() {
		return payment_details;
	}

	public void setPayment_details(String payment_details) {
		this.payment_details = payment_details;
	}

	public String getPayment_risk() {
		return payment_risk;
	}

	public void setPayment_risk(String payment_risk) {
		this.payment_risk = payment_risk;
	}

	public String getOrder_notes() {
		return order_notes;
	}

	public void setOrder_notes(String order_notes) {
		this.order_notes = order_notes;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public String getPayment_country() {
		return payment_country;
	}

	public void setPayment_country(String payment_country) {
		this.payment_country = payment_country;
	}

	@Override
	public String toString() {
		return "OceanPaymentResult [response_type=" + response_type
				+ ", order_number=" + order_number + ", account=" + account
				+ ", terminal=" + terminal + ", signValue=" + signValue
				+ ", payment_status=" + payment_status + ", order_currency="
				+ order_currency + ", order_amount=" + order_amount
				+ ", card_number=" + card_number + ", payment_id=" + payment_id
				+ ", payment_authType=" + payment_authType
				+ ", payment_details=" + payment_details + ", payment_risk="
				+ payment_risk + ", order_notes=" + order_notes + "]";
	}

}

package forms.order;

import play.data.validation.Constraints.Required;

public class ReplaceOrder {
	@Required
	String paymentId;
	@Required
	Integer orderId;
	@Required
	String corderId;

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getCorderId() {
		return corderId;
	}

	public void setCorderId(String corderId) {
		this.corderId = corderId;
	}

}

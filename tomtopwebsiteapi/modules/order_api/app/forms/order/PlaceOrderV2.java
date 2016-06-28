package forms.order;

import play.data.validation.Constraints.Required;

public class PlaceOrderV2 {
	@Required
	Integer addressId;
	Integer billId;
	@Required
	Integer shippingMethodIdValue;
	String cartId;
	String message;
	@Required
	String paymentId;
	
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getShippingMethodIdValue() {
		return shippingMethodIdValue;
	}

	public void setShippingMethodIdValue(Integer shippingMethodIdValue) {
		this.shippingMethodIdValue = shippingMethodIdValue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	@Override
	public String toString() {
		return "PlaceOrder [addressId=" + addressId + ", billId=" + billId
				+ ", shippingMethodId=" + shippingMethodIdValue + ", cartId="
				+ cartId + ", message=" + message + "]";
	}

}

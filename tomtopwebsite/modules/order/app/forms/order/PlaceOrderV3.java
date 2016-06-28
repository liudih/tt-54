package forms.order;


import play.data.validation.Constraints;

public class PlaceOrderV3 {

	@Constraints.Required
	Integer addressId;
	Integer billId;

	Integer shippingMethodIdValue;
	String cartId;
	String message;

	@Constraints.Required
	String paymentId;
	
	String shipMethodCode;
	Integer shipMethodId;

	public String getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getCartId() {
		return this.cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public Integer getAddressId() {
		return this.addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getShippingMethodIdValue() {
		return this.shippingMethodIdValue;
	}

	public void setShippingMethodIdValue(Integer shippingMethodIdValue) {
		this.shippingMethodIdValue = shippingMethodIdValue;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getBillId() {
		return this.billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String toString() {
		return "PlaceOrder [addressId=" + this.addressId + ", billId="
				+ this.billId + ", shippingMethodId="
				+ this.shippingMethodIdValue + ", cartId=" + this.cartId
				+ ", message=" + this.message + "]";
	}

	public String getShipMethodCode() {
		return shipMethodCode;
	}

	public void setShipMethodCode(String shipMethodCode) {
		this.shipMethodCode = shipMethodCode;
	}

	public Integer getShipMethodId() {
		return shipMethodId;
	}

	public void setShipMethodId(Integer shipMethodId) {
		this.shipMethodId = shipMethodId;
	}
	
}

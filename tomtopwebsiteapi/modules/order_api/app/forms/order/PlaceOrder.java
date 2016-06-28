package forms.order;

import play.data.validation.Constraints.Required;

public class PlaceOrder {

	Integer addressId;
	Integer billId;
	@Required
	Integer shippingMethodId;
	@Required
	String cartId;
	String message;

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

	public Integer getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(Integer shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
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
				+ ", shippingMethodId=" + shippingMethodId + ", cartId="
				+ cartId + ", message=" + message + "]";
	}

}

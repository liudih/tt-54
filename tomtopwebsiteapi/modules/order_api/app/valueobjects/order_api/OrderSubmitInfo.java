package valueobjects.order_api;

import java.util.List;

public class OrderSubmitInfo {
	private Integer addressID;
	private Integer billAddressID;
	private List<ShippingIDWithOrder> shippingIDs;
	private String message;
	private String cartID;

	public Integer getAddressID() {
		return addressID;
	}

	public void setAddressID(Integer addressID) {
		this.addressID = addressID;
	}

	public List<ShippingIDWithOrder> getShippingIDs() {
		return shippingIDs;
	}

	public void setShippingIDs(List<ShippingIDWithOrder> shippingIDs) {
		this.shippingIDs = shippingIDs;
	}

	public Integer getBillAddressID() {
		return billAddressID;
	}

	public void setBillAddressID(Integer billAddressID) {
		this.billAddressID = billAddressID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCartID() {
		return cartID;
	}

	public void setCartID(String cartID) {
		this.cartID = cartID;
	}

}

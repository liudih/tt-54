package forms.order;


import play.data.validation.Constraints;

public class PlaceOrderV3 {

	@Constraints.Required
	Integer addressId;
	Integer billId;

	String message;

	String shipMethodCode;
	Integer shipMethodId;

	public Integer getAddressId() {
		return this.addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
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

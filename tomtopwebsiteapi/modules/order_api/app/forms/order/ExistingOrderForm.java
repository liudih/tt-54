package forms.order;

import play.data.validation.Constraints.Required;

public class ExistingOrderForm {
	@Required
	private Boolean isSelect;
	@Required
	private String orderID;
	private Integer addressId;
	private Integer shippingMethodId;
	private String message;	//留言

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
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

}

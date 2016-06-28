package form.wholesale;

import play.data.validation.Constraints.Required;

public class WholesaleOrderForm {
	@Required
	private String productIDs;
	@Required
	private Boolean isShipping;
	private Integer addressID;

	public String getProductIDs() {
		return productIDs;
	}

	public void setProductIDs(String productIDs) {
		this.productIDs = productIDs;
	}

	public Boolean getIsShipping() {
		return isShipping;
	}

	public void setIsShipping(Boolean isShipping) {
		this.isShipping = isShipping;
	}

	public Integer getAddressID() {
		return addressID;
	}

	public void setAddressID(Integer addressID) {
		this.addressID = addressID;
	}

}

package forms.order;

import play.data.validation.Constraints.Required;

public class DropShippingOrderForm {
	@Required
	private String ids;
	@Required
	private String shippingMethodIDs;
	@Required
	private String dropShippingID;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getShippingMethodIDs() {
		return shippingMethodIDs;
	}

	public void setShippingMethodIDs(String shippingMethodIDs) {
		this.shippingMethodIDs = shippingMethodIDs;
	}

	public String getDropShippingID() {
		return dropShippingID;
	}

	public void setDropShippingID(String dropShippingID) {
		this.dropShippingID = dropShippingID;
	}

}

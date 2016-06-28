package dto;

import java.io.Serializable;

public class ProductMultiattributeItem implements Serializable {
	private static final long serialVersionUID = 1L;

	String parentSku;

	String listingId;

	public String getParentSku() {
		return parentSku;
	}

	public void setParentSku(String parentSku) {
		this.parentSku = parentSku;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

}

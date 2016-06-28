package valueobjects.order_api.cart;

import java.io.Serializable;

import services.IFoundationService;

public class CartContext implements Serializable {
	private static final long serialVersionUID = 1L;
	private final IFoundationService foundation;
	private final String listingID;

	public CartContext(IFoundationService foundation, String listingID) {
		this.foundation = foundation;
		this.listingID = listingID;
	}

	public IFoundationService getFoundation() {
		return foundation;
	}

	public String getListingID() {
		return listingID;
	}

}

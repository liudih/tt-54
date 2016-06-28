package events.product;

import java.io.Serializable;

public class SaleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	final String listingId;

	final int websitid;

	public SaleEvent(String listingId, int websitid) {
		super();
		this.listingId = listingId;
		this.websitid = websitid;
	}

	public String getListingId() {
		return listingId;
	}

	public int getWebsitid() {
		return websitid;
	}
}

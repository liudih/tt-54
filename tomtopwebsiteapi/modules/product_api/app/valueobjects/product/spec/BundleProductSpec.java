package valueobjects.product.spec;

import java.io.Serializable;
import java.util.List;

public class BundleProductSpec implements IProductSpec, Serializable {

	private static final long serialVersionUID = -2993160827485305255L;

	final String mainListingID;
	final List<String> allListingIDs;

	final int qty;

	public BundleProductSpec(String mainListingID, List<String> allListingIDs,
			int qty) {
		super();
		this.mainListingID = mainListingID;
		this.allListingIDs = allListingIDs;
		this.qty = qty;
	}

	@Override
	public String getListingID() {
		return mainListingID;
	}

	@Override
	public int getQty() {
		return qty;
	}

	public List<String> getAllListingIDs() {
		return allListingIDs;
	}

	@Override
	public String toString() {
		return "BundleProductSpec(" + mainListingID + ", " + allListingIDs
				+ ", " + qty + ")";
	}

}

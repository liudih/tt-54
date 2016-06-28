package valueobjects.product.spec;

import java.io.Serializable;

public class SingleProductSpec implements IProductSpec, Serializable {

	private static final long serialVersionUID = -6011313501554892382L;

	final String listingID;

	final int qty;

	public SingleProductSpec(String listingID, int qty) {
		super();
		this.listingID = listingID;
		this.qty = qty;
	}

	public String getListingID() {
		return listingID;
	}

	public int getQty() {
		return qty;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((listingID == null) ? 0 : listingID.hashCode());
		result = prime * result + qty;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleProductSpec other = (SingleProductSpec) obj;
		if (listingID == null) {
			if (other.listingID != null)
				return false;
		} else if (!listingID.equals(other.listingID))
			return false;
		if (qty != other.qty)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SingleProductSpec(" + listingID + ", " + qty + ")";
	}

}

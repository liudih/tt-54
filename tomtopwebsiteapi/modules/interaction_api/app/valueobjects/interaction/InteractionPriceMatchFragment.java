package valueobjects.interaction;

import valueobjects.product.IProductFragment;

public class InteractionPriceMatchFragment implements IProductFragment {
	private String listingid;
	private String sku;
	private String email;
	public InteractionPriceMatchFragment(String listingid, String sku,
			String email) {
		super();
		this.listingid = listingid;
		this.sku = sku;
		this.email = email;
	}
	public String getListingid() {
		return listingid;
	}
	public void setListingid(String listingid) {
		this.listingid = listingid;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}

package events.order;

public class ProductsaleEvent {

	private final int siteId;

	public ProductsaleEvent(int siteId) {
		this.siteId = siteId;
	}

	public int getSiteId() {
		return siteId;
	}

}

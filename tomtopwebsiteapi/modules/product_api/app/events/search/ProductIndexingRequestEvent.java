package events.search;

import java.util.List;

public class ProductIndexingRequestEvent {

	final List<String> listingIds;

	public ProductIndexingRequestEvent(List<String> listingIds) {
		this.listingIds = listingIds;
	}

	public List<String> getListingIds() {
		return listingIds;
	}

}

package valueobjects.product;

import java.util.List;
import java.util.Map;

public class ProductCustomAttributes implements IProductFragment {

	final String currentListingID;
	final Map<String, List<ProductAttributeTag>> tags;

	public ProductCustomAttributes(String currentListingID,
			Map<String, List<ProductAttributeTag>> tags) {
		super();
		this.currentListingID = currentListingID;
		this.tags = tags;
	}

	public String getCurrentListingID() {
		return currentListingID;
	}

	public Map<String, List<ProductAttributeTag>> getTags() {
		return tags;
	}
}

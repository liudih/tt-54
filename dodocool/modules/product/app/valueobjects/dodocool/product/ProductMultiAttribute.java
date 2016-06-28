package valueobjects.dodocool.product;

import java.util.List;
import java.util.Map;

import valueobjects.product.IProductFragment;
import dto.product.ProductMultiattributeEntity;

public class ProductMultiAttribute implements IProductFragment {

	final Map<String, List<ProductMultiattributeEntity>> maps;
	final String currentListingId;

	public ProductMultiAttribute(
			Map<String, List<ProductMultiattributeEntity>> maps,
			String currentListingId) {
		this.maps = maps;
		this.currentListingId = currentListingId;
	}

	public Map<String, List<ProductMultiattributeEntity>> getAttributeMap() {
		return maps;
	}

	public String getCurrentListingId() {
		return currentListingId;
	}
}

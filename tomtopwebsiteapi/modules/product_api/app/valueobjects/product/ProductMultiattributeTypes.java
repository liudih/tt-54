package valueobjects.product;

import java.util.List;

public class ProductMultiattributeTypes implements IProductFragment {
	final List<dto.ProductMultiattributeType> ProductMultiattributeTypes;

	public ProductMultiattributeTypes(
			List<dto.ProductMultiattributeType> ProductMultiattributeTypes) {
		this.ProductMultiattributeTypes = ProductMultiattributeTypes;
	}

	public List<dto.ProductMultiattributeType> getProductMultiattributeType() {
		return ProductMultiattributeTypes;
	}

}

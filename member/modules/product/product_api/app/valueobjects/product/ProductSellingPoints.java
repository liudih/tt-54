package valueobjects.product;

import java.util.List;

public class ProductSellingPoints implements IProductFragment {

	final List<dto.product.ProductSellingPoints> productSellingPoints;

	public ProductSellingPoints(
			List<dto.product.ProductSellingPoints> sellingPoints) {
		this.productSellingPoints = sellingPoints;
	}

	public List<dto.product.ProductSellingPoints> getProductSellingPoints() {
		return productSellingPoints;
	}
}

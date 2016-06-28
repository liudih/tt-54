package valueobjects.product;

import dto.ProductSalePriceLite;

public class ProductSalePrice implements IProductFragment {

	final ProductSalePriceLite productSalePrices;

	public ProductSalePrice(ProductSalePriceLite productSalePrices) {
		this.productSalePrices = productSalePrices;
	}

	public ProductSalePriceLite getProductSalePrices() {
		return productSalePrices;
	}
	
}

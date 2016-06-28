package valueobjects.product;

import valueobjects.price.Price;

public class ProductBase implements IProductFragment {

	final dto.product.ProductBase productBase;
	final Price price;

	public ProductBase(dto.product.ProductBase base, Price price) {
		this.productBase = base;
		this.price = price;
	}

	public dto.product.ProductBase getProductBase() {
		return productBase;
	}

	public Price getPrice() {
		return price;
	}

}

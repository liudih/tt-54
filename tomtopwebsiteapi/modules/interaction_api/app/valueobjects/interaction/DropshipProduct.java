package valueobjects.interaction;

import valueobjects.product.ProductBadge;

public class DropshipProduct {

	private dto.interaction.DropshipProduct product;
	private ProductBadge ProductBadge;

	public DropshipProduct(dto.interaction.DropshipProduct dropshipProduct,
			valueobjects.product.ProductBadge productBadge) {
		this.product = dropshipProduct;
		ProductBadge = productBadge;
	}

	public dto.interaction.DropshipProduct getProduct() {
		return product;
	}

	public void setProduct(dto.interaction.DropshipProduct product) {
		this.product = product;
	}

	public ProductBadge getProductBadge() {
		return ProductBadge;
	}

	public void setProductBadge(ProductBadge productBadge) {
		ProductBadge = productBadge;
	}

}

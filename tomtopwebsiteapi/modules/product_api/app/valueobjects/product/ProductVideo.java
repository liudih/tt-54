package valueobjects.product;

import java.util.List;

public class ProductVideo implements IProductFragment {

	final List<dto.product.ProductVideo> productVoideos;

	public ProductVideo(List<dto.product.ProductVideo> productVideos) {
		this.productVoideos = productVideos;
	}

	public List<dto.product.ProductVideo> getProductVideos() {
		return productVoideos;
	}
}

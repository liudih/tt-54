package valueobjects.product;

import java.util.List;

public class ProductGroupPrice implements IProductFragment {

    final List<dto.ProductGroupPriceLite> productGroupPrices;

    public ProductGroupPrice(List<dto.ProductGroupPriceLite> productGroupPrices) {
        this.productGroupPrices = productGroupPrices;
    }

    public List<dto.ProductGroupPriceLite> getProductGroupPrices() {
        return productGroupPrices;
    }

}

package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dto.ProductGroupPriceLite;
import mapper.product.ProductGroupPriceMapper;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class ProductGroupPriceFragmentProvider implements IProductFragmentProvider {

    public static final String NAME = "group-price";

    @Inject
    ProductGroupPriceMapper productGroupPriceMapper;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public IProductFragment getFragment(ProductContext context, Map<String, Object> processingContext) {
        List<ProductGroupPriceLite> productGroupPrices = productGroupPriceMapper.getProductGroupPriceByListingId(context
                .getListingID(),context.getCurrency());
        return new valueobjects.product.ProductGroupPrice(productGroupPrices);
    }

}

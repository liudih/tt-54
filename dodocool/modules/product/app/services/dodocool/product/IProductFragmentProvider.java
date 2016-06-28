package services.dodocool.product;

import java.util.Map;

import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public interface IProductFragmentProvider {

	IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext);

}

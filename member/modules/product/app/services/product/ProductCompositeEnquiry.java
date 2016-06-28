package services.product;

import java.util.Set;

import javax.inject.Inject;

import valueobjects.product.ProductComposite;
import valueobjects.product.ProductContext;

public class ProductCompositeEnquiry {

	@Inject
	Set<IProductFragmentPlugin> fragmentPlugins;

	public ProductComposite getProductComposite(ProductContext context) {
		ProductComposite composite = new ProductComposite(context);
		for (IProductFragmentPlugin fp : fragmentPlugins) {
			IProductFragmentProvider provider = fp.getFragmentProvider();
			if (provider != null) {
				composite.put(fp.getName(), provider.getFragment(context,
						composite.getAttributes()));
			}
		}
		return composite;
	}

}

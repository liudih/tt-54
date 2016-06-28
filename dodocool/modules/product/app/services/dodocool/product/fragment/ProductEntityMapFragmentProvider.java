package services.dodocool.product.fragment;

import java.util.Map;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.dodocool.product.IProductFragmentProvider;
import services.product.IEntityMapService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductEntityMap;
import valueobjects.product.ProductContext;
import context.ContextUtils;
import context.WebContext;

public class ProductEntityMapFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	IEntityMapService entityMapService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		Map<String, String> entityMap = entityMapService.getAttributeMap(
				context.getListingID(), webContext);
		return new ProductEntityMap(entityMap);
	}

}

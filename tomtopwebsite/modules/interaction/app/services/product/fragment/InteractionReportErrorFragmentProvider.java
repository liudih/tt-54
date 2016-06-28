package services.product.fragment;

import java.util.Map;

import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

public class InteractionReportErrorFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "report_error";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		return null;
	}

}

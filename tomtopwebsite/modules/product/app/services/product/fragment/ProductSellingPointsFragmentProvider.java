package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import dao.product.IProductSellingPointsEnquiryDao;
import dto.product.ProductSellingPoints;

public class ProductSellingPointsFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "selling-points";

	@Inject
	IProductSellingPointsEnquiryDao productSellingPointsEnquityDao;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<ProductSellingPoints> sellingPoints = this.productSellingPointsEnquityDao
				.getProductSellingPointsByListingIdAndLanguage(
						context.getListingID(), context.getLang());
		return new valueobjects.product.ProductSellingPoints(sellingPoints);
	}

}

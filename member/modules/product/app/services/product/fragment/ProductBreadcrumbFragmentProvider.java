package services.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductCategoryMapperMapper;
import services.product.CategoryEnquiryService;
import services.product.IProductFragmentProvider;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.category.CategoryReverseComposite;
import dto.product.ProductCategoryMapper;

public class ProductBreadcrumbFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	ProductCategoryMapperMapper mapper;

	@Inject
	CategoryEnquiryService enquiry;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<ProductCategoryMapper> categories = mapper
				.selectByListingId(context.getListingID());
		if (categories != null && categories.size() > 0) {
			CategoryReverseComposite rev = enquiry.getReverseCategory(
					categories.get((categories.size()-1)).getIcategory(), context.getLang(),
					context.getSiteID());
			return rev;
		}
		return null;
	}

}

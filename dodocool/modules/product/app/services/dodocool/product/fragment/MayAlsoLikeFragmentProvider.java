package services.dodocool.product.fragment;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.dodocool.product.IProductFragmentProvider;
import services.dodocool.product.ProductMessageService;
import services.product.IProductCategoryEnquiryService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

import com.google.api.client.util.Lists;

import context.ContextUtils;
import dto.product.ProductMessage;

public class MayAlsoLikeFragmentProvider implements IProductFragmentProvider {

	@Inject
	IProductCategoryEnquiryService productCategoryEnquiryService;

	@Inject
	ProductMessageService productMessageService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<Integer> categoryIds = (List) processingContext.get("categoryIds");
		List<ProductMessage> productMessages = Lists.newArrayList();
		if (categoryIds != null && 0 < categoryIds.size()) {
			List<String> listingIds = productCategoryEnquiryService
					.getListingIds(categoryIds, 20, 1);
			productMessages = productMessageService.getProductMessages(
					listingIds, ContextUtils.getWebContext(Context.current()));
		}
		processingContext.put("productMessages", productMessages);
		if (null != productMessages && 0 < productMessages.size()) {
			processingContext.put("show-also-like", true);
		} else {
			processingContext.put("show-also-like", false);
		}

		return null;
	}

}

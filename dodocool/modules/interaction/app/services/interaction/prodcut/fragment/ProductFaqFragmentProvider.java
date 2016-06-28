package services.interaction.prodcut.fragment;

import java.util.List;
import java.util.Map;

import play.mvc.Http.Context;
import services.dodocool.base.FoundationService;
import services.dodocool.product.IProductFragmentProvider;
import services.interaction.product.post.IProductPostService;
import valueobjects.base.Page;
import valueobjects.dodocool.interaction.product.InteractionProductPost;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;

public class ProductFaqFragmentProvider implements IProductFragmentProvider {

	@Inject
	IProductPostService productPostService;

	@Inject
	FoundationService foundationService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {

		String listingId = context.getListingID();
		String sku = context.getSku();
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		Integer itypeid = 1; //1表示faq
		Integer state = 1;//1表示已审核
		Integer page = 1;
		Integer pageSize = 10;

		Page<Map<String, List<dto.interaction.ProductPost>>> faqPage = productPostService
				.getProductPostList(listingId, webContext, itypeid, state,
						page, pageSize);
		Boolean isLogined = foundationService.isLogined();
		return new InteractionProductPost(faqPage, listingId, isLogined, sku);
	}
}

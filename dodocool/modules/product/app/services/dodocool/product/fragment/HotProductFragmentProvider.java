package services.dodocool.product.fragment;

import java.util.List;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.dodocool.base.template.ITemplateFragmentProvider;
import services.dodocool.product.ProductMessageService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.ProductLabelType;
import services.search.criteria.ProductTagsCriteria;
import valueobjects.base.Page;
import valueobjects.search.SearchContext;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.ProductMessage;

public class HotProductFragmentProvider implements ITemplateFragmentProvider {
	@Inject
	ISearchContextFactory factory;

	@Inject
	ISearchService searchService;

	@Inject
	ProductMessageService productMessageService;

	@Override
	public String getName() {
		return "hot-product-sale";
	}

	@Override
	public Html getFragment(Context context) {
		SearchContext searchContext = factory
				.pureSearch(new ProductTagsCriteria(ProductLabelType.Hot
						.toString()));
		WebContext webContext = ContextUtils.getWebContext(context.current());
		searchContext.setPageSize(5);
//		Page<String> listingsProm = searchService.searchByContext(
//				searchContext, webContext);
//		List<String> listingids = listingsProm.getList();
//		List<ProductMessage> productMessages = productMessageService
//				.getProductMessages(listingids, webContext);

		return views.html.product.category.hot_sale.render(null);
	}

}

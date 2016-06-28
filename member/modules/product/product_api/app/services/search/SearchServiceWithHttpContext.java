package services.search;

import javax.inject.Inject;

import play.mvc.Http.Context;
import valueobjects.base.Page;
import valueobjects.search.SearchContext;
import context.WebContext;

public class SearchServiceWithHttpContext implements ISearchService {

	@Inject
	ISearchService service;

	public Page<String> search(SearchContext context, int siteId, int languageId) {
		saveContext(context);
		return service.search(context, siteId, languageId);
	}

	public Page<String> searchByContext(SearchContext context,
			WebContext webContext) {
		saveContext(context);
		return service.searchByContext(context, webContext);
	}

	public <T> Page<T> searchByP(SearchContext context, int siteId,
			int languageId, SearchResultExtractor<T> extractor) {
		saveContext(context);
		return service.searchByP(context, siteId, languageId, extractor);
	}

	public <T> Page<T> searchPByContext(SearchContext context,
			WebContext webContext, SearchResultExtractor<T> extractor) {
		saveContext(context);
		return service.searchPByContext(context, webContext, extractor);
	}

	public Page<String> getProductByKeyWord(SearchContext context,
			WebContext webContext) {
		saveContext(context);
		return service.getProductByKeyWord(context, webContext);
	}

	private void saveContext(SearchContext context) {
		Context.current().args.put("searchContext", context);
	}

}

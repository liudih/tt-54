package services.search;

import valueobjects.base.Page;
import valueobjects.search.SearchContext;
import context.WebContext;

public interface ISearchService {

	Page<String> search(SearchContext context, int siteId, int languageId);

	Page<String> searchByContext(SearchContext context, WebContext webContext);

	<T> Page<T> searchByP(SearchContext context, int siteId, int languageId,
			SearchResultExtractor<T> extractor);

	<T> Page<T> searchPByContext(SearchContext context, WebContext webContext,
			SearchResultExtractor<T> extractor);

	Page<String> getProductByKeyWord(SearchContext context,
			WebContext webContext);

}
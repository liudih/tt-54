package services.search;

import context.WebContext;
import valueobjects.search.SearchContext;
import play.libs.F;
import valueobjects.base.Page;

public interface IAsyncSearchService {

	public abstract F.Promise<Page<String>> asyncSearch(SearchContext context,
			int siteId, int languageId);
	
	public abstract Page<String> getasynSearch(SearchContext context,WebContext webContext);

}
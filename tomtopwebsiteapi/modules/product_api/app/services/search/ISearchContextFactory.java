package services.search;

import java.util.Map;
import java.util.Set;

import valueobjects.search.ISearchCriteria;
import valueobjects.search.ISearchFilter;
import valueobjects.search.ISortOrder;
import valueobjects.search.SearchContext;

public interface ISearchContextFactory {

	public SearchContext fromCurrentHttpContext(ISearchCriteria mainCriteria);

	public SearchContext pureSearch(ISearchCriteria mainCriteria);

	public SearchContext fromQueryCurrentHttpContext(
			ISearchCriteria mainCriteria, Map<String, String[]> queryStrings);

	public SearchContext fromQueryString(ISearchCriteria mainCriteria,
			Map<String, String[]> queryStrings, Set<String> ulfilters);

	public SearchContext personalfromQueryString(ISearchCriteria mainCriteria,
			Map<String, String[]> queryStrings);

	public SearchContext personalfromQueryString(ISearchCriteria mainCriteria);

	public SearchContext personalSearch(ISearchCriteria initCriteria,
			ISortOrder initOrder, ISearchFilter initFilter,
			final Set<String> ulfilters);

	public SearchContext lastSearchContext();
}

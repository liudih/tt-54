package valueobjects.search;

import valueobjects.search.SearchContext;
import play.twirl.api.Html;

public interface ISearchUIProvider {

	int getDisplayOrder();

	String getName();
	
	Html getHtml(SearchContext context);

}

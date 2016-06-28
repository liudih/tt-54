package extensions.product.search;

import java.io.Serializable;

import valueobjects.search.SearchContext;
import play.twirl.api.Html;
import valueobjects.search.ISearchUIProvider;

public class PagerUI implements ISearchUIProvider,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getDisplayOrder() {
		return 200;
	}

	@Override
	public String getName(){
		return "pager";
	}
	
	@Override
	public Html getHtml(SearchContext context) {
		return views.html.product.search.pager.render(context);
	}

}

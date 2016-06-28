package extensions.product.search;

import java.io.Serializable;

import valueobjects.search.SearchContext;
import play.twirl.api.Html;
import valueobjects.search.ISearchUIProvider;

public class PriceRangeUI implements ISearchUIProvider,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getDisplayOrder() {
		return 110;
	}

	@Override
	public String getName(){
		return "pricerange";
	}
	
	
	@Override
	public Html getHtml(SearchContext context) {
		return views.html.product.search.pricerange.render(context);
	}
}

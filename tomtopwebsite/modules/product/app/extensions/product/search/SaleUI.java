package extensions.product.search;

import java.io.Serializable;

import valueobjects.search.SearchContext;
import play.twirl.api.Html;
import valueobjects.search.ISearchUIProvider;

public class SaleUI implements ISearchUIProvider,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getDisplayOrder() {
		return 60;
	}

	@Override
	public String getName() {
		return "sale";
	}

	@Override
	public Html getHtml(SearchContext context) {
		return views.html.product.search.sale.render(context);
	}
}

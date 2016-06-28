package extensions.product.search;

import java.io.Serializable;

import enumtype.ProductLabelType;
import valueobjects.search.SearchContext;
import play.twirl.api.Html;
import valueobjects.search.ISearchUIProvider;

public class FreeShippingUI implements ISearchUIProvider, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public String getName() {
		return ProductLabelType.FreeShipping.toString();
	}

	@Override
	public Html getHtml(SearchContext context) {
		return views.html.product.search.freeshipping.render(context);
	}
}

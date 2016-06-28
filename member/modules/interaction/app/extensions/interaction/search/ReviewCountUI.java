package extensions.interaction.search;

import java.io.Serializable;

import valueobjects.search.SearchContext;
import play.twirl.api.Html;
import valueobjects.search.ISearchUIProvider;

public class ReviewCountUI implements ISearchUIProvider,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getDisplayOrder() {
		return 70;
	}

	@Override
	public String getName(){
		return "reviewcount";
	}
	
	@Override
	public Html getHtml(SearchContext context) {
		return views.html.interaction.review.review.render(context);
	}

}

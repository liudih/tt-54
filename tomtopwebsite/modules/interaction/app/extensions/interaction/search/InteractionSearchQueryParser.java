package extensions.interaction.search;

import java.util.Map;

import valueobjects.search.SearchContext;
import play.Logger;
import valueobjects.search.ISearchQueryParser;
import extensions.interaction.search.sort.ReviewCountSortOrder;

public class InteractionSearchQueryParser implements ISearchQueryParser {

	@Override
	public void parse(Map<String, String[]> queryStrings, SearchContext context) {
		if (queryStrings.containsKey("review")) {
			boolean asc = false;
			String[] values = queryStrings.get("review");
			if (values != null && values.length > 0) {
				asc = ("asc".equalsIgnoreCase(values[0]));
			}
			Logger.debug("Setting Sort by Review Count: asc={}", asc);
			context.getSort().add(new ReviewCountSortOrder(asc));
		}
	}

}

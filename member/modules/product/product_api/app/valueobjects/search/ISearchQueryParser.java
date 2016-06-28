package valueobjects.search;

import java.util.Map;

import valueobjects.search.SearchContext;

/**
 * 从QueryString变成适当的SearchContext，用来构建最终的SearchContext
 * 
 * @author kmtong
 *
 */
public interface ISearchQueryParser {

	void parse(Map<String, String[]> queryStrings, SearchContext context);

}

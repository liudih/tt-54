package valueobjects.search;

import org.elasticsearch.index.query.QueryBuilder;

public abstract class ISearchKeyWords implements ISearchCriteria {

	private static final long serialVersionUID = 751419665655430828L;

	public abstract String getKeyWord(String keyword);

	@Override
	public abstract QueryBuilder getQueryBuilder();

}

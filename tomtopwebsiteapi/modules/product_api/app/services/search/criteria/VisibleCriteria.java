package services.search.criteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchCriteria;

public class VisibleCriteria implements ISearchCriteria {

	private static final long serialVersionUID = -4963838412265649940L;

	boolean visible;

	public VisibleCriteria() {

	}

	public VisibleCriteria(boolean visible) {
		this.visible = visible;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return QueryBuilders.termQuery("visible", visible);
	}
}

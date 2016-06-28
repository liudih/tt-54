package services.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchCriteria;

public class StatusSearch implements ISearchCriteria {

	private static final long serialVersionUID = 2912316869586246982L;

	@Override
	public QueryBuilder getQueryBuilder() {
		BoolQueryBuilder bool = QueryBuilders.boolQuery();
		bool.must(QueryBuilders.termQuery("status", 1)).must(QueryBuilders.termQuery("visible", true));
		return bool;
	}

}

package services.dodocool.search.criteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import services.search.criteria.ProductLabelType;
import valueobjects.search.ISearchCriteria;

public class NewArrivalsSearchCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 1L;

	public NewArrivalsSearchCriteria() {
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("tags",
						ProductLabelType.NewArrial.toString()));
		return query;
	}
}

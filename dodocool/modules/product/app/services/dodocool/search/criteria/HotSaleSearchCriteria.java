package services.dodocool.search.criteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import services.search.criteria.ProductLabelType;
import valueobjects.search.ISearchCriteria;

public class HotSaleSearchCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 1L;

	public HotSaleSearchCriteria() {
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("tags",
						ProductLabelType.Hot.toString()));
		return query;
	}
}

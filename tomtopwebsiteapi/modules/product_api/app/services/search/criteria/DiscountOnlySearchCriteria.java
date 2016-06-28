package services.search.criteria;

import java.util.Date;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import valueobjects.search.ISearchCriteria;

public class DiscountOnlySearchCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 1L;

	public DiscountOnlySearchCriteria() {
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		long now = new Date().getTime();
		RangeQueryBuilder fromRange = QueryBuilders.rangeQuery(
				"price.discount.fromDate").lte(now);
		RangeQueryBuilder toRange = QueryBuilders.rangeQuery(
				"price.discount.toDate").gte(now);
		RangeQueryBuilder price = QueryBuilders.rangeQuery(
				"price.discount.discount").gte(0.2);
		QueryBuilder query = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.nestedQuery("price.discount", QueryBuilders
						.boolQuery().must(fromRange).must(toRange).must(price)))
				.mustNot(
						QueryBuilders.termQuery("tags",
								ProductLabelType.Clearstocks.toString()));
		return query;
	}
}

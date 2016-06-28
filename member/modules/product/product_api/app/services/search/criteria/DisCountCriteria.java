package services.search.criteria;

import java.util.Date;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import valueobjects.search.ISearchCriteria;

import com.google.common.collect.Range;

public class DisCountCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 1L;

	Range<Double> disRange;

	public DisCountCriteria(Range<Double> disRange) {
		this.disRange = disRange;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		Double from = disRange.lowerEndpoint().doubleValue();
		Double to = disRange.upperEndpoint().doubleValue();

		RangeQueryBuilder discountRange = QueryBuilders
				.rangeQuery("price.discount.discount").lte(to).gte(from);

		long now = new Date().getTime();
		RangeQueryBuilder fromRangeDate = QueryBuilders.rangeQuery(
				"price.discount.fromDate").lte(now);
		RangeQueryBuilder toRangeDate = QueryBuilders.rangeQuery(
				"price.discount.toDate").gte(now);
		QueryBuilder query = QueryBuilders
				.boolQuery()
				.must(QueryBuilders.nestedQuery("price.discount", QueryBuilders
						.boolQuery().must(discountRange).must(fromRangeDate)
						.must(toRangeDate)))
				.mustNot(
						QueryBuilders.termQuery("tags",
								ProductLabelType.Clearstocks.toString()));
		return query;
	}
}

package services.search.criteria;

import java.util.Date;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import valueobjects.search.ISearchCriteria;

import com.google.common.collect.Range;

/**
 * Please use DiscountDateFilter instead.
 * 
 * @author kmtong
 * @deprecated
 * @see DiscountDateFilter
 */
public class DiscountDateCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 1L;

	Range<Date> dateRange;

	public DiscountDateCriteria() {
	}

	public DiscountDateCriteria(Range<Date> dateRange) {
		if (!dateRange.hasLowerBound() || !dateRange.hasUpperBound()) {
			throw new RuntimeException(
					"Date range must have lower bound and upper bound");
		}
		this.dateRange = dateRange;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		Date from = dateRange.lowerEndpoint();
		Date to = dateRange.upperEndpoint();

		RangeQueryBuilder fromRange = QueryBuilders
				.rangeQuery("price.discount.fromDate").lte(to.getTime())
				.gte(from.getTime());
		RangeQueryBuilder toRange = QueryBuilders
				.rangeQuery("price.discount.toDate").lte(to.getTime())
				.gte(from.getTime());
		return QueryBuilders.nestedQuery("price.discount", QueryBuilders
				.boolQuery().must(fromRange).must(toRange));
	}

}

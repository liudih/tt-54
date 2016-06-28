package services.search.criteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import valueobjects.search.ISearchCriteria;

import com.google.common.collect.Range;

/**
 * This class is deprecated, please use PriceRangeFilter instead.
 * 
 * @author kmtong
 * @deprecated
 * @see PriceRangeFilter
 */
public class PriceRangeCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 7229103617116981770L;

	Range<Double> priceRange;

	public PriceRangeCriteria() {
	}

	public PriceRangeCriteria(Range<Double> priceRange) {
		this.priceRange = priceRange;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		RangeQueryBuilder baseRange = QueryBuilders
				.rangeQuery("price.basePrice");
		RangeQueryBuilder discountRange = QueryBuilders
				.rangeQuery("price.discount.price");
		setupRange(baseRange);
		setupRange(discountRange);
		return QueryBuilders.boolQuery().should(baseRange)
				.should(discountRange);
	}

	private void setupRange(RangeQueryBuilder range) {
		if (priceRange.hasLowerBound()) {
			switch (priceRange.lowerBoundType()) {
			case OPEN:
				range.gt(priceRange.lowerEndpoint());
				break;
			case CLOSED:
				range.gte(priceRange.lowerEndpoint());
				break;
			}
		}
		if (priceRange.hasUpperBound()) {
			switch (priceRange.upperBoundType()) {
			case OPEN:
				range.lt(priceRange.upperEndpoint());
				break;
			case CLOSED:
				range.lte(priceRange.upperEndpoint());
				break;
			}
		}
	}

	public Range<Double> getPriceRange() {
		return priceRange;
	}

	@Override
	public String toString() {
		return "PriceRangeCriteria [priceRange=" + priceRange + "]";
	}

}

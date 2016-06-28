package services.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import valueobjects.search.ISearchCriteria;
import com.google.common.collect.Range;

public class FreeShippingCriteria implements ISearchCriteria {

	private static final long serialVersionUID = -192939124355194496L;

	Range<Double> weight;
	Range<Double> price;

	public FreeShippingCriteria() {

	}

	public FreeShippingCriteria(double weights,double prices) {
		this.weight = Range.closedOpen(0.0, weights);
		this.price=Range.greaterThan(prices);
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		BoolQueryBuilder bool = QueryBuilders.boolQuery();
		RangeQueryBuilder weightValue = QueryBuilders.rangeQuery("weight");
		setupRange(weightValue);
		RangeQueryBuilder priceValue =QueryBuilders.rangeQuery("price.basePrice");
		setupRangePrice(priceValue);
		bool.must(weightValue).must(priceValue);
		return bool;
	}

	private void setupRange(RangeQueryBuilder range) {
		if (weight.hasLowerBound()) {
			switch (weight.lowerBoundType()) {
			case OPEN:
				range.gt(weight.lowerEndpoint());
				break;
			case CLOSED:
				range.gte(weight.lowerEndpoint());
				break;
			}
		}
		if (weight.hasUpperBound()) {
			switch (weight.upperBoundType()) {
			case OPEN:
				range.lt(weight.upperEndpoint());
				break;
			case CLOSED:
				range.lte(weight.upperEndpoint());
				break;
			}
		}
	}
	
	private void setupRangePrice(RangeQueryBuilder rangeprice) {
		if (price.hasLowerBound()) {
			switch (price.lowerBoundType()) {
			case OPEN:
				rangeprice.gt(price.lowerEndpoint());
				break;
			case CLOSED:
				rangeprice.gte(price.lowerEndpoint());
				break;
			}
		}
		if (price.hasUpperBound()) {	
			switch (price.upperBoundType()) {
			case OPEN:
				rangeprice.lt(price.upperEndpoint());
				break;
			case CLOSED:
				rangeprice.lte(price.upperEndpoint());
				break;
			}
		}
	}
}

package services.search.filter;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.ScriptFilterBuilder;

import valueobjects.search.ISearchFilter;

import com.google.common.collect.Range;

public class PriceRangeFilter implements ISearchFilter {

	private static final long serialVersionUID = 7229103617116981770L;

	Range<Double> priceRange;

	public PriceRangeFilter() {
	}

	public PriceRangeFilter(Range<Double> priceRange) {
		this.priceRange = priceRange;
	}

	private String setupRange(String variable) {
		StringBuilder sb = new StringBuilder();
		if (priceRange.hasLowerBound()) {
			switch (priceRange.lowerBoundType()) {
			case OPEN:
				sb.append(variable + ">" + priceRange.lowerEndpoint());
				break;
			case CLOSED:
				sb.append(variable + ">=" + priceRange.lowerEndpoint());
				break;
			}
		}
		if (priceRange.hasUpperBound()) {
			switch (priceRange.upperBoundType()) {
			case OPEN:
				if (sb.length() > 0) {
					sb.append("&&");
				}
				sb.append(variable + "<" + priceRange.upperEndpoint());
				break;
			case CLOSED:
				if (sb.length() > 0) {
					sb.append("&&");
				}
				sb.append(variable + "<=" + priceRange.upperEndpoint());
				break;
			}
		}
		return sb.toString();
	}

	public Range<Double> getPriceRange() {
		return priceRange;
	}

	@Override
	public String toString() {
		return "PriceRangeFilter [priceRange=" + priceRange + "]";
	}

	@Override
	public FilterBuilder getFilter() {
		String price = "def d = _source.price.discount.grep { "
				+ "it -> (new Date()).getTime() > it.fromDate && (new Date()).getTime() < it.toDate "
				+ "}*.price;"
				+ "def actualPrice = (d.size() > 0 "
				+ "? (d.first() > _source.price.basePrice ? _source.price.basePrice : d.first()) "
				+ ": _source.price.basePrice);";
		String range = "return " + setupRange("actualPrice");
		ScriptFilterBuilder actualPrice = FilterBuilders.scriptFilter(price
				+ range);
		return actualPrice;
	}
}

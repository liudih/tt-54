package services.search.filter;

import java.util.Date;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

import valueobjects.search.ISearchFilter;

import com.google.common.collect.Range;

public class DiscountDateFilter implements ISearchFilter {

	private static final long serialVersionUID = 1L;

	Range<Date> dateRange;

	public DiscountDateFilter() {
	}

	public DiscountDateFilter(Range<Date> dateRange) {
		if (!dateRange.hasLowerBound() || !dateRange.hasUpperBound()) {
			throw new RuntimeException(
					"Date range must have lower bound and upper bound");
		}
		this.dateRange = dateRange;
	}

	@Override
	public FilterBuilder getFilter() {
		long from = dateRange.lowerEndpoint().getTime();
		long to = dateRange.upperEndpoint().getTime();

		String discountDate = "def d = _source.price.discount.grep { "
				+ "it -> " + from + " >= it.fromDate && " + from + "< it.toDate &&" +
				to	+ " >= it.toDate " + "};" + "return (d.size() > 0);";

		return FilterBuilders.scriptFilter(discountDate);
	}

}

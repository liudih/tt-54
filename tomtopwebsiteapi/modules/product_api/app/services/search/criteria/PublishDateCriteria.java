package services.search.criteria;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import valueobjects.search.ISearchCriteria;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

public class PublishDateCriteria implements ISearchCriteria {

	private static final long serialVersionUID = -112296853714812229L;

	List<Range<Date>> dateRanges;

	public PublishDateCriteria() {
	}

	@SuppressWarnings("unchecked")
	public PublishDateCriteria(Range<Date> dateRange) {
		this.dateRanges = Lists.newArrayList(dateRange);
	}

	public PublishDateCriteria(List<Date> dates) {
		this.dateRanges = Lists.transform(dates, d -> Range.closedOpen(
				DateUtils.truncate(d, Calendar.DATE),
				DateUtils.addDays(DateUtils.truncate(d, Calendar.DATE), 1)));
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		BoolQueryBuilder bool = QueryBuilders.boolQuery();
		for (Range<Date> r : dateRanges) {
			RangeQueryBuilder range = QueryBuilders.rangeQuery("createdate");
			setupRange(range, r);
			bool.should(range);
		}
		return bool;
	}

	private void setupRange(RangeQueryBuilder rangeQuery, Range<Date> dateRange) {
		if (dateRange.hasLowerBound()) {
			switch (dateRange.lowerBoundType()) {
			case OPEN:
				rangeQuery.gt(dateRange.lowerEndpoint().getTime());
				break;
			case CLOSED:
				rangeQuery.gte(dateRange.lowerEndpoint().getTime());
				break;
			}
		}
		if (dateRange.hasUpperBound()) {
			switch (dateRange.upperBoundType()) {
			case OPEN:
				rangeQuery.lt(dateRange.upperEndpoint().getTime());
				break;
			case CLOSED:
				rangeQuery.lte(dateRange.upperEndpoint().getTime());
				break;
			}
		}
	}
	
	public Range<Date> getDateRangeFirst() {
		return this.dateRanges.get(0);
	}

}

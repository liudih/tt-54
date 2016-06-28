package services.search.aggs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import valueobjects.search.ISearchAggValue;
import valueobjects.search.ISearchAggregation;
import valueobjects.search.agg.CategorySearchAggValue;

public class CategorysSearchAggregation implements ISearchAggregation, Serializable {

	private static final long serialVersionUID = 1L;
	
	final String KEYVAL = "categorys";

	@Override
	public AbstractAggregationBuilder getAggBuilder() {
		return AggregationBuilders.terms(KEYVAL).field("categories");
	}

	@Override
	public ISearchAggValue getAggValue(SearchResponse response) {
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		CategorySearchAggValue cvalue = new CategorySearchAggValue(map);
		if (response.getAggregations() != null) {
			Terms t = response.getAggregations().get(KEYVAL);
			t.getBuckets().forEach(p -> {
				map.put(p.getKeyAsNumber().intValue(), p.getDocCount());
			});
		}
		return cvalue;
	}
}

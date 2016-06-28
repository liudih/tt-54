package com.tomtop.es.services.index;


import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/ _bucket_aggregations.html#java-aggs-bucket-filter
 * 
 * @author Administrator
 *
 */
public interface ISearchAggregation {

	/**
	 * 创建统计条件
	 * @return
	 */
	AbstractAggregationBuilder getAggBuilder();

	/**
	 * 转换统计结果
	 * @param response
	 * @return
	 */
	ISearchAggValue getAggValue(SearchResponse response);
	
	/**
	 * 多条件统计
	 * @param list 需要集合的属性集合
	 * @return
	 */
	List<AbstractAggregationBuilder> getAggBuilders(List<String> list);
}

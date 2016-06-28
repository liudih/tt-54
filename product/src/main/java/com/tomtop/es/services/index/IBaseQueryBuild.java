package com.tomtop.es.services.index;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

import com.tomtop.es.entity.Filter;
import com.tomtop.es.entity.PageBean;

public interface IBaseQueryBuild {

	 /**
	  * 构造SearchRequestBuilder
	  * @param bean
	  * @return
	  */
	 public SearchRequestBuilder getRequestBuilder(PageBean bean);
	 
	  /**
	   * 根据关键字去构造QueryBuilder
	   * @param key
	   * @return
	   */
	  public QueryBuilder buildQueryBuilder(String key);
	  
	  /**
	   * 准确匹配 不分词 完全匹配
	   * @param proName 属性名称
	   * @param proVal  属性值
	   * @return TermQueryBuilder
	   */
	  public TermQueryBuilder accurateMatch(String proName,Object proValue);
	  
	  /**
	   * 多个属性精准匹配 不分词 完全匹配 属性之间的关系为Must
	   * @param map<属性键值对>
	   * @return
	   */
	  public QueryBuilder accurateMustMatch(Map<String,Object> map);
	  
	  /**
	   * 多个属性匹配同一个值，属性之间的关系为should
	   * @param proNames 属性名称集合
	   * @param value 属性值
	   * @return
	   */
	  public QueryBuilder accurateShouldMatch(List<String> proNames ,String value);
	  
	  /**
	   * 添加排序属性
	   * @param requestBuilder
	   * @param orderName
	   * @param order
	   * @return
	   */
	  public FieldSortBuilder getSort(String orderName,String order);
	  
	  
	  /**
	   * 构造查询BoolQueryBuilder
	   * @param key 关键字key
	   * @param list 过滤条件
	   * @return
	   */
	  public QueryBuilder buildQueryBuilder(String key,List<Filter> list);
	  
	  
	  /**
	   * 将查询结果集转成实体类
	   * @param hits  
	   * @param clazz
	   * @return
	   */
	  public  <T> List<T> handerHit(SearchHits hits,Class<T> clazz);
	  
	  /**
	   * 将查询结果集转成实体类
	   * @param hits  
	   * @param bean
	   * @param count
	   * @return
	   */
	  public PageBean handerHit(SearchHits hits,PageBean bean,long count);
	  
	  /**
	   * 聚合
	   * @param map
	   * @return
	   */
	  public List<AbstractAggregationBuilder> getAggreationgBuilders(List<Filter> filters);
}

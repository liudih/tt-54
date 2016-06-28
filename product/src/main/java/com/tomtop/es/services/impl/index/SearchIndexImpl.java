package com.tomtop.es.services.impl.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tomtop.es.entity.Filter;
import com.tomtop.es.entity.IndexEntity;
import com.tomtop.es.entity.PageBean;
import com.tomtop.es.services.index.ISearchIndex;

/**
 * 索引查询
 * @author ztiny
 * @Date 2015-12-19
 */
@Component
public class SearchIndexImpl  extends BaseQueryBuildImpl implements ISearchIndex {

	Logger logger = Logger.getLogger(SearchIndexImpl.class);
	
	/**
	 * 返回索引的ID
	 * @param indexName 索引名称
	 * @param indexType 索引类型
	 * @param hashMap 查询关键字
	 * @return
	 */
	@Override
	public List<String> search(String indexName,String indexType,Map<String,Object> hashMap){
		if(hashMap==null || hashMap.size()<1){
			return null;
		}
		List<String> ids = new ArrayList<String>();
		try{
			SearchRequestBuilder requestBuilder = this.getRequestBuilder(indexName,indexType);
			requestBuilder.setQuery(accurateMustMatch(hashMap));
			SearchHits hits = requestBuilder.execute().actionGet().getHits();
			for (SearchHit searchHit : hits) {
				ids.add(searchHit.getId());
			} 
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ids;
	}

	
	

	/**
	 * 查询
	 * @param bean
	 * @return
	 */
	public PageBean queryBykey(PageBean bean){
		int prodtypeId = 0;
		if(bean.getFilters()!=null && bean.getFilters().size()>0){
			for (Filter filter : bean.getFilters()) {
				if(filter.getPropetyName().equals("mutil.productTypes.productTypeId")){
					Object obj = filter.getPropertyValue();
					prodtypeId = obj==null?0:(Integer)obj;
					break;
				}
			}
		}
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		//添加过滤条件
		QueryBuilder queryBuilder = this.buildQueryBuilder(bean.getKeyword(),bean.getFilters());
		requestBuilder = requestBuilder.setQuery(queryBuilder);
		long count = this.getCount(requestBuilder);
		//排序
		requestBuilder = this.autoOrder(requestBuilder,bean,prodtypeId);
		//聚合
		requestBuilder = this.addAggreationgBuilders(requestBuilder, bean.getFilters(),bean.getRangeAgg(),prodtypeId).setSize(1000);
		SearchHits hits = null;
		try{
			SearchResponse response = requestBuilder.setFrom(bean.getBeginNum()).setSize(bean.getEndNum()).execute().actionGet();
			bean = handler(bean,response.getAggregations(),prodtypeId);
			hits = response.getHits();
		}catch(Exception ex){
			ex.printStackTrace();
		}
 		
		return this.handerHit(hits, bean,count);
	}
	

	
	
	/**
	 * 返回索引的PageBean，该方法适用于 not_analyzed 类型的字段查询
	 * @param bean 
	 * @param hashMap 查询关键字<属性名称,属性值>
	 * @return
	 */
	@Override
	public PageBean searchByKey(PageBean bean, Map<String, Object> hashMap) {
		if(hashMap==null || hashMap.size()<1){
			return null;
		}
		try{
			SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean.getIndexName(),bean.getIndexType());
			requestBuilder.setQuery(accurateMustMatch(hashMap));
			SearchHits hits = requestBuilder.execute().actionGet().getHits();
			long count = hits.getTotalHits();
			bean = this.handerHit(hits, bean,count);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 推荐查询
	 * @param bean
	 * @param proName 属性名称
	 * @return
	 */
	public PageBean queryMoreLikeThis(PageBean bean,String proName){
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		BoolQueryBuilder boolQuery = this.buildQueryBuilderByFilters(bean.getFilters());
		if(StringUtils.isBlank(proName)){
			proName = "mutil.title";
		}
		MoreLikeThisQueryBuilder moreLikeQuery = QueryBuilders.moreLikeThisQuery(proName).like(bean.getKeyword()).analyzer("standard").boost(0.8f).minTermFreq(1).maxQueryTerms(12).minDocFreq(1);
		requestBuilder = requestBuilder.setQuery(boolQuery.must(moreLikeQuery));
		requestBuilder = this.autoOrder(requestBuilder,bean);
		SearchHits hits = requestBuilder.setFrom(bean.getBeginNum()).setSize(bean.getEndNum()).execute().actionGet().getHits();
		logger.debug(requestBuilder.toString());
		long count = hits.getTotalHits();
		return  this.handerHit(hits, bean,count);
	}
	
	
	
	/**
	 *  属性匹配多个值的精准查询
	 *  @param indexName 索引名称
	 *  @param indexType 索引类型
	 *  @param proName 属性名称
	 *  @param filters 过滤条件
	 *  @param objs 属性对应的多个值
	 */
	@SuppressWarnings("unchecked")
	public PageBean queryTermsAnyValue(PageBean bean,String proName){
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean.getIndexName(),bean.getIndexType());
		BoolQueryBuilder boolQuery = this.buildQueryBuilderByFilters(bean.getFilters());
		ArrayList<String> values  =  new ArrayList<String>() ;
		SearchHits hits = null;
		try{
			values = JSON.parseObject(bean.getKeyword(), ArrayList.class);
			hits = requestBuilder.setQuery(boolQuery.must(this.accurateMatch(proName,values))).execute().actionGet().getHits();
		}catch(Exception ex){
			hits = requestBuilder.setQuery(boolQuery.must(this.accurateMatch(proName, bean.getKeyword()))).execute().actionGet().getHits();
//			ex.printStackTrace();
		}
		
		long count = hits.getTotalHits();
		return this.handerHit(hits, bean,count);
	}
	
	
	/**
	 * 根据多个listingid查询
	 * @param indexName
	 * @param indexType
	 * @param ids listingid的集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageBean queryByIds(PageBean bean){
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean.getIndexName(), bean.getIndexType());
		ArrayList<String> ids  =  JSON.parseObject(bean.getKeyword(), ArrayList.class);
		SearchHits hits = requestBuilder.setQuery(QueryBuilders.idsQuery().ids(ids)).setSize(ids.size()).execute().actionGet().getHits();
		long count = hits.getTotalHits();
		return this.handerHit(hits, bean, count);
	}




	@Override
	public IndexEntity queryById(String ListingId,String indexName,String indexType) {
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(indexName,indexType);
		SearchHits hits = requestBuilder.setQuery(QueryBuilders.idsQuery().ids(ListingId)).execute().actionGet().getHits();
		return this.handerHit(hits);
	}




}

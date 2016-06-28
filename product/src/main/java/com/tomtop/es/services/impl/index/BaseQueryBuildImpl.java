package com.tomtop.es.services.impl.index;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filters.InternalFilters;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.es.BaseClient;
import com.tomtop.es.common.BaseServiceUtil;
import com.tomtop.es.common.DateUtil;
import com.tomtop.es.entity.AggregationEntity;
import com.tomtop.es.entity.Filter;
import com.tomtop.es.entity.IndexEntity;
import com.tomtop.es.entity.OrderEntity;
import com.tomtop.es.entity.PageBean;
import com.tomtop.es.entity.RangeAggregation;
import com.tomtop.es.entity.RemoteAttributeEntity;
import com.tomtop.es.entity.RemoteProductType;
import com.tomtop.es.services.index.IBaseQueryBuild;;
/**
 * 构造查询条件
 * @author ztiny
 * @Date 2015-12-24
 */
public abstract class BaseQueryBuildImpl implements IBaseQueryBuild{
	
	static Logger logger = Logger.getLogger(BaseQueryBuildImpl.class);
	/**搜索的字段**/
	public  String []fields = {"listingId","sku","brand","mutil.title","mutil.productTypes.productTypeName","mutil.keywords","mutil.items.value","spu"};
	
	@Autowired
	private BaseClient esClient;
	
	@Autowired
	private BaseServiceUtil baseUtil;
	
	
	public  String[] getFields() {
		return fields;
	}

	public  void setFields(String[] fields) {
		this.fields = fields;
	}

	public SearchRequestBuilder getRequestBuilder(PageBean bean) {
		Assert.notNull(bean, "beand对象为空");
		Assert.notNull(bean.getIndexNames(),"索引名称为空");
		Assert.notNull(bean.getIndexType(),"索引类型为空");
		Client client =esClient.getSearchClient();
		return client.prepareSearch(bean.getIndexName()).setTypes(bean.getIndexType());
	}
	
	public SearchRequestBuilder getRequestBuilder(String indexName,String indexType) {
		Assert.notNull(indexName, "indexName对象为空");
		Assert.notNull(indexType,"indexType名称为空");
		Client client =esClient.getSearchClient();
		SearchRequestBuilder requestBuilder = client.prepareSearch(indexName).setTypes(indexType);
		return requestBuilder;
	}
	
	
	
	
	
	/**
	 * 多字段查询
	 * @param key 关键字
	 * @param fields 对象属性名称的数组
	 * @return
	 */
	public QueryBuilder multiMatchQuery(String key,String ...fields){
		
//		String fs[] = {"listingId","sku","brand","mutil.productTypes.productTypeName","mutil.keywords","mutil.items.value","spu"};
		return QueryBuilders.multiMatchQuery(key,fields).field("mutil.title", 10);
	}
	
	/**
	 * 匹配所有字段 模糊查询
	 * @return
	 */
	public QueryBuilder mathcAll(){
		return QueryBuilders.matchAllQuery();
	}
	
	/**
	 * 准确匹配 不分词 完全匹配
	 * @param proName 属性名称
	 * @param proVal  属性值
	 * @return TermQueryBuilder
	 */
	public TermQueryBuilder accurateMatch(String proName,Object proValue){
		return  QueryBuilders.termQuery(proName, proValue);
	}
	
	/**
	 * 准确匹配 不分词 完全匹配
	 * @param proName 属性名称
	 * @param values  属性值数组
	 * @return TermsQueryBuilder
	 */
	public TermsQueryBuilder accurateMatch(String proName,Object ...values){
		return  QueryBuilders.termsQuery(proName, values);
	}
	/**
	 * 多个属性精准匹配 不分词 完全匹配 属性之间的关系为Must
	 * @param map<属性键值对>
	 * @return
	 */
	public QueryBuilder accurateMustMatch(Map<String,Object> map){
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {  
            Map.Entry<String, Object> entry = it.next();  
            TermQueryBuilder termQuery = accurateMatch(entry.getKey(),entry.getValue());
            queryBuilder.must(termQuery);
        }  
		return queryBuilder;
	}
	
	/**
	 * 多个属性匹配同一个值，属性之间的关系为should
	 * @param proNames 属性名称集合
	 * @param value 属性值
	 * @return
	 */
	public QueryBuilder accurateShouldMatch(List<String> proNames ,String value){
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		for (String field : proNames) {
			queryBuilder.should(accurateMatch(field,value));
		}
		return queryBuilder;
	}
	
	
	/**
	 * 根据SKU、listingId、brand 查询 字段之间的关键为should 至少有个匹配
	 * @param boolQuery
	 * @param key 关键字
	 * @return
	 */
	public BoolQueryBuilder buildSearchQuery(BoolQueryBuilder boolQuery,String key){
		
		return boolQuery.should(QueryBuilders.termQuery("sku", key)).should(QueryBuilders.termQuery("listingId", key)).should(QueryBuilders.termQuery("brand", key));
	}
	
	
	/**
	 * 添加排序属性
	 * @param requestBuilder
	 * @param orderName
	 * @param order
	 * @return
	 */
	public FieldSortBuilder getSort(String orderName,String order){
		FieldSortBuilder sortBuilder = new FieldSortBuilder(orderName);
		if(StringUtils.equals(order, PageBean.order.asc.toString())){
			sortBuilder.order(SortOrder.ASC);
		}else{
			sortBuilder.order(SortOrder.DESC);
		}
		return sortBuilder;
	}
	
	/**
	 * 构造查询QueryBuilder
	 * @param key 关键字key
	 * @param list 过滤条件
	 * @return QueryBuilder
	 */
	public QueryBuilder buildQueryBuilder(String key,List<Filter> list){
		if(list==null || list.size()<1){
			return this.buildQueryBuilder(key);
		}
		
		String fields[] ={"listingId","sku","brand","mutil.productTypes.productTypeName","mutil.keywords","mutil.items.value","spu"};
		
		QueryBuilder queryBuilder = null;
		if(StringUtils.isNotBlank(key)){
			queryBuilder = multiMatchQuery(key,fields);
		}else{
			queryBuilder = this.mathcAll();
		}
		
		return this.buildQueryBuilderByFilters(list).must(queryBuilder);
	}
	
	/**
	 * 构造查询QueryBuilder
	 * @param key 关键字key
	 * @return QueryBuilder
	 */
	public QueryBuilder buildQueryBuilder(String key){
		if(StringUtils.isNotBlank(key)){
			return  multiMatchQuery(key,fields);
		}else{
			return  mathcAll();
		}
	}

	/**
	 * 添加过滤条件
	 * @param filter 过滤条件
	 * @return
	 */
	public BoolQueryBuilder buildQueryByFilter(Filter filter){
		return null;
	}
	
	
	/**
	 * 添加过滤条件
	 * @param list 过滤条件
	 * @return
	 */
	public BoolQueryBuilder buildQueryBuilderByFilters(List<Filter> list){
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		for (Filter filter : list) {
			if(filter.isFilter()){
				String proName = filter.getPropetyName();
				if(StringUtils.equals("tagsName.tagName",proName) && StringUtils.equals(filter.getPropertyValue().toString().toLowerCase(), "onsale")){
					String pattern = "yyyy-MM-dd";
					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
					String currentDate = sdf.format(new Date());
					boolQuery.must(QueryBuilders.rangeQuery("promotionPrice.beginDate").format(pattern).lte(currentDate))
					.must(QueryBuilders.rangeQuery("promotionPrice.endDate").format(pattern).gte(currentDate));
				} else if(StringUtils.equals("tagsName.tagName",proName) && StringUtils.equals(filter.getPropertyValue().toString().toLowerCase(), "freeshipping")){
					boolQuery.must(QueryBuilders.termsQuery("tagsName.tagName", "freeshipping","allfreeshipping"));
				}else{
					String proValues[]={};
					Object proValue = filter.getPropertyValue();
					if(proValue!=null && !(proValue instanceof Boolean)){
						proValues = proValue.toString().split(",");
					}
					if(StringUtils.equals(Filter.character.and.get(),filter.getExpress()) ||StringUtils.equals(Filter.character.eq.get(),filter.getExpress()) ){
						//判断是一个属性对应多个值，如果是则用should，且必须满足一个条件
						if(proValues.length>1){
							for (String pvalue : proValues) {
								boolQuery.should(QueryBuilders.termQuery(proName,pvalue));
							}
							boolQuery.minimumNumberShouldMatch(1);
						}else{
								//如果单个属性过滤，则用must匹配
								boolQuery.must(QueryBuilders.termQuery(proName,proValue));
						}
					}else if(StringUtils.equals(Filter.character.or.get(),filter.getExpress())){
						if(proValues.length>1){
							for (String pvalue : proValues) {
								boolQuery.should(QueryBuilders.termQuery(proName,pvalue));
							}
						}else{
							boolQuery.should(QueryBuilders.termQuery(proName,filter.getPropertyValue()));
						}
					}else if(StringUtils.equals(Filter.character.not.get(),filter.getExpress())){
						if(proValues.length>1){
							for (String pvalue : proValues) {
								boolQuery.mustNot(QueryBuilders.termQuery(proName,pvalue));
							}
						}else{
							boolQuery.mustNot(QueryBuilders.termQuery(filter.getPropetyName(),filter.getPropertyValue()));
						}
					}else if(StringUtils.equals(Filter.character.ge.get(),filter.getExpress())){
						 boolQuery.filter(QueryBuilders.rangeQuery(filter.getPropetyName()).gte(filter.getPropertyValue()));
					}else if(StringUtils.equals(Filter.character.gt.get(),filter.getExpress())){
						 boolQuery.filter(QueryBuilders.rangeQuery(filter.getPropetyName()).gt(filter.getPropertyValue()));
					}else if(StringUtils.equals(Filter.character.lt.get(),filter.getExpress())){
						 boolQuery.filter(QueryBuilders.rangeQuery(filter.getPropetyName()).lt(filter.getPropertyValue()));
					}else if(StringUtils.equals(Filter.character.le.get(),filter.getExpress())){
						 boolQuery.filter(QueryBuilders.rangeQuery(filter.getPropetyName()).lte(filter.getPropertyValue()));
					}
				}
				
				
				
			}
		}
		return boolQuery;
	}
	
	
	/**
	 * 将查询结果集转成实体类
	 * @param hits	
	 * @param clazz
	 * @return
	 */
	public  <T> List<T> handerHit(SearchHits hits,Class<T> clazz){
		if(hits==null || hits.getHits().length<1){
			return new ArrayList<T>();
		}
		List<T> list = new ArrayList<T>();
		for (SearchHit searchHit : hits) {
			Map<String, Object> result = searchHit.getSource();
			String resutlJson = JSONObject.toJSONString(result);
			T t = JSON.parseObject(resutlJson, clazz);
			list.add(t);
		}
		return list;
	}
	
	/**
	 * 解析聚合出来的结果集
	 * @param bean
	 * @param aggreations
	 * @return
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	public PageBean handler(PageBean bean,Aggregations aggreations,Integer prodtypeId){
		if(aggreations ==null){
			return bean;
		}
		
		try{
		Map<String, Aggregation> map = aggreations.getAsMap();
//		Map<String, RemoteProductType> typeMap = BaseServiceUtil.getAllProductType();
		Map<String, RemoteProductType> typeMap = baseUtil.getAllProductType();
		List<AggregationEntity> temps = new ArrayList<AggregationEntity>();
		for (Map.Entry<String, Aggregation> entry : map.entrySet()) {  
			Object key = entry.getValue();
			String keyName = entry.getKey();
			//判断是否为范围类型的聚合
			if(key instanceof InternalRange){
				try{
					InternalRange range = aggreations.get(entry.getKey());
					List<InternalRange.Bucket> list = range.getBuckets();
					if(list!=null && list.get(0)!=null){
						InternalRange.Bucket bucket = list.get(0);
						
						AggregationEntity agg = new AggregationEntity();
						if(bucket.getDocCount()>0){
							agg.setCount(bucket.getDocCount());
							agg.setName(bucket.getKey());
							//存入数组后在转换成list
							temps.add(agg);
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}else if(key instanceof InternalFilter){
				InternalFilter filter = aggreations.get(keyName);
				AggregationEntity agg = new AggregationEntity();
				agg.setCount(filter.getDocCount());
				agg.setName(keyName);
				//判断是否onSale标签
				if(entry.getKey().equals("onSale")){
					//如果是，则把tagsName标签对应的集合取出来，把onSale的结果一同放进去
					List<AggregationEntity> aggs =  bean.getAggsMap().get("tagsName.tagName");
					if(aggs==null ){
						aggs = new ArrayList<AggregationEntity>();
					}
					aggs.add(agg);
					if(aggs!=null && aggs.size()>0){
						bean.getAggsMap().put("tagsName.tagName", aggs);
					}
				}
			}else if(key instanceof InternalFilters){
				InternalFilters filters = aggreations.get(keyName);
				
				
				List<InternalFilters.Bucket> filterBicukets = filters.getBuckets();
				List<AggregationEntity> aggs =  bean.getAggsMap().get(keyName);
				if(aggs==null ){
					aggs = new ArrayList<AggregationEntity>();
				}
				for (InternalFilters.Bucket bucket : filterBicukets) {
					AggregationEntity agg = new AggregationEntity();
					agg.setCount(bucket.getDocCount());
					String typeName =bucket.getKeyAsString();
					agg.setName(typeName);
					if(agg.getCount()>0){
						aggs.add(agg);
					}
				}
				if(aggs!=null && aggs.size()>0){
					bean.getAggsMap().put(keyName, aggs);
				}
				
			}else{
				Terms term = aggreations.get(keyName);
				List<Bucket> bicukets = term.getBuckets();
				List<AggregationEntity> newAggs = setAggs(bicukets,keyName,prodtypeId,typeMap);
				List<AggregationEntity> aggs =  bean.getAggsMap().get(keyName);
				if(aggs==null ){
					aggs = new ArrayList<AggregationEntity>();
				}
				aggs.addAll(newAggs);
				if(aggs!=null && aggs.size()>0){
					bean.getAggsMap().put(keyName, aggs);
				}
			}
		
		}
		
		//价格冒泡排序
		if(temps!=null && temps.size()>0){
			List<RangeAggregation> oldList = bean.getRangeAgg();
				for (int i=0;i<oldList.size();i++) {
					RangeAggregation rangeModel = oldList.get(i);
					for(int j =i;j<temps.size();j++){
						AggregationEntity m1 = temps.get(j);
						AggregationEntity m2 = temps.get(i);
						if(m1.getName().equals(rangeModel.getAliasName())){
							temps.set(j, m2);
							temps.set(i, m1);
							break;
						}
					}
				}
			
			bean.getAggsMap().put("yjPrice", temps);
		}
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bean;
	}
	
	
	
	
	private List<AggregationEntity> setAggs(List<Bucket> buckets,String keyName,Integer prodtypeId,Map<String, RemoteProductType> typeMap){
		List<AggregationEntity> termAggs = new ArrayList<AggregationEntity>();
		for (Terms.Bucket bucket : buckets) {
			//用于标识该聚合条件是否返回给调用者
			boolean flag = true;
			AggregationEntity agg = new AggregationEntity();
			agg.setCount(bucket.getDocCount());
			String typeName =bucket.getKeyAsString();
			
			//远程获取类目名称
			if(StringUtils.equals(keyName, "mutil.productTypes.productTypeId")){
				agg.setId(Long.parseLong(bucket.getKeyAsString()));
				RemoteProductType model = baseUtil.getAllProductType().get(bucket.getKeyAsString());
				if((model!=null && model.getIparentid().intValue()==prodtypeId.intValue())){
					//聚合当前类目下的子类目
					flag = true;
					typeName = model.getCname();
					agg.setCpath(model.getCpath());
				}else {
					flag = false;
				}
			}else if(StringUtils.equals(keyName, "tagsName.tagName")){
				//只展示freeshipping的标签
				typeName = bucket.getKeyAsString();
				if(!typeName.equals("freeshipping")){
					flag = false;
				}
			}
			agg.setName(typeName);
			if(flag){
				termAggs.add(agg);
			}
		}

		return termAggs;
		
	}
	
	public   PageBean handerHit(SearchHits hits,PageBean bean,long count){
		if(hits==null || hits.getHits().length<1){
			return bean;
		}
		List<IndexEntity> list = new ArrayList<IndexEntity>();
		for (SearchHit searchHit : hits) {
			Map<String, Object> result = searchHit.getSource();
			String resutlJson = JSONObject.toJSONString(result);
			IndexEntity t = JSON.parseObject(resutlJson, IndexEntity.class);
			list.add(t);
		}
		bean.setTotalCount(count);
		bean.getIndexs().addAll(list);
		return bean;
	}
	
	public   IndexEntity handerHit(SearchHits hits){
		if(hits==null || hits.getHits().length<1){
			return null;
		}
		IndexEntity t = null;
		for (SearchHit searchHit : hits) {
			Map<String, Object> result = searchHit.getSource();
			String resutlJson = JSONObject.toJSONString(result);
			t = JSON.parseObject(resutlJson, IndexEntity.class);
			if(t!=null){
				break;
			}
		}
		return t;
	}
	
	/**
	 * 多属性排序
	 * @param requestBuilder
	 * @param order
	 * @return
	 */
	private SearchRequestBuilder autoOrder(SearchRequestBuilder requestBuilder,List<OrderEntity> orders,int productTypeId){
		Assert.notNull(requestBuilder, "requestBuilder为空");
		for (OrderEntity orderEntity : orders) {
			//当类目id>0,且排序属性名称匹配sort字段时
			if(productTypeId>0 && StringUtils.equals(orderEntity.getPropetyName(), "mutil.productTypes.sort")){
				
				// 根据类目ID获取它的类目级别，然后在进行过滤
//				RemoteProductType model = BaseServiceUtil.getProductType(productTypeId);
				RemoteProductType model = baseUtil.getAllProductType().get(String.valueOf(productTypeId));
				FieldSortBuilder fieldSort = null;
				TermQueryBuilder termFilter = null;
				if(model!=null){
					if(model.getIlevel()==1){
						termFilter = QueryBuilders.termQuery("mutil.productLevel1.productTypeId", productTypeId);
						fieldSort = SortBuilders.fieldSort("mutil.productLevel1.sort");
					}else if(model.getIlevel()==2){
						termFilter = QueryBuilders.termQuery("mutil.productLevel2.productTypeId", productTypeId);
						fieldSort = SortBuilders.fieldSort("mutil.productLevel2.sort");
					}else if(model.getIlevel()==3){
						termFilter = QueryBuilders.termQuery("mutil.productLevel3.productTypeId", productTypeId);
						fieldSort = SortBuilders.fieldSort("mutil.productLevel3.sort");
					}
				}
				if(fieldSort!=null){
					BoolQueryBuilder bool= QueryBuilders.boolQuery().must(termFilter);
					fieldSort.setNestedFilter(bool).order(SortOrder.ASC);
					requestBuilder.addSort(fieldSort);
				}
			}else if(StringUtils.equals(SortOrder.ASC.toString(), orderEntity.getType())){
				requestBuilder.addSort(orderEntity.getPropetyName(), SortOrder.ASC);
			}else if(StringUtils.equals(SortOrder.DESC.toString(), orderEntity.getType())){
				requestBuilder.addSort(orderEntity.getPropetyName(), SortOrder.DESC);
			}
		}
		return requestBuilder;
	}
	
	/**
	 * 多属性排序,PageBean 里面排序的属性永远排第一
	 * @param requestBuilder
	 * @param bean
	 * @return
	 */
	public SearchRequestBuilder autoOrder(SearchRequestBuilder requestBuilder,PageBean bean,int productTypeId){
		Assert.notNull(requestBuilder, "requestBuilder为空");
		if( bean==null){
			return requestBuilder;
		}
		List<OrderEntity> orders = bean.getOrders();
		//如果排序集合里面已经存在排序属性，则该排序属性不参与后续的排序
		if((orders==null || orders.isEmpty()) && StringUtils.isNotBlank(bean.getOrderName())){
			String orderName = bean.getOrderName();
			String orderValue = bean.getOrderValue();
			OrderEntity order = new OrderEntity(orderName,-1,orderValue);
			if(order!=null){
				orders.add(order);
			}
		}
		return autoOrder(requestBuilder,bean.getOrders(),productTypeId);
	}
	
	public SearchRequestBuilder autoOrder(SearchRequestBuilder requestBuilder,PageBean bean){
		Assert.notNull(requestBuilder, "requestBuilder为空");
		if( bean==null){
			return requestBuilder;
		}
		List<OrderEntity> orders = bean.getOrders();
		//如果排序集合里面已经存在排序属性，则该排序属性不参与后续的排序
		if((orders==null || orders.isEmpty()) && StringUtils.isNotBlank(bean.getOrderName())){
			String orderName = bean.getOrderName();
			String orderValue = bean.getOrderValue();
			OrderEntity order = new OrderEntity(orderName,-1,orderValue);
			if(order!=null){
				orders.add(order);
			}
		}
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
		return autoOrder(requestBuilder,bean.getOrders(),prodtypeId);
	}
	
	
	
	 /**
	  * 统计总数
	  * @param requestBuilder
	  * @return
	  */
	 public long getCount(SearchRequestBuilder requestBuilder){
		 Assert.notNull(requestBuilder, "requestBuilder为空");
		 return requestBuilder.execute().actionGet().getHits().getTotalHits();
	 }
	 
	 
	 /**
	  * 对索引id加上国际化
	  * @param indexName
	  * @param ids
	  * @return
	  */
	 public IdsQueryBuilder ids(String indexName,ArrayList<String> ids){
		 List<String> keys = new ArrayList<String>();
		 String domain = indexName.substring(indexName.lastIndexOf("_"),indexName.length());
		 for (String string : ids) {
			 string = ids + domain;
			 keys.add(string);
		}
		return QueryBuilders.idsQuery().ids(keys);
	 }
	 
	/**
	 * 需要聚合的属性 
	 */
	public List<AbstractAggregationBuilder> getAggreationgBuilders(List<Filter> filters){
		return null;
	}
	
	/**
	 * 聚合
	 * @param requestBuilder
	 * @param filters
	 * @return
	 */
	public SearchRequestBuilder addAggreationgBuilders(SearchRequestBuilder requestBuilder,List<Filter> filters,List<RangeAggregation> aggs,int productTypeId){
		if(filters==null || filters.size()<1){
			return requestBuilder;
		}
		for (Filter filter : filters) {
			if(filter.isAgg()){
				if(filter.getPropetyName().equals("mutil.items.value")){
					productTypeId =864;
					//根据类目找到所有的key
					Map<String,List<RemoteAttributeEntity>> map = baseUtil.getAllShowKeyByProductTypeId(productTypeId);
					//所有的value
					Map<String,List<String>> valuesMap = baseUtil.getAllShowValues();
					if(map!=null && map.size()>0){
						//遍历所有的key进行聚合
						for (Map.Entry<String, List<RemoteAttributeEntity>> entry : map.entrySet()) {
							if(entry.getValue()!=null){
								Object obj = entry.getValue();
								JSONArray arr = (JSONArray)obj;
								for (Object object : arr) {
									String type = ((JSONObject)object).get("attributeValue").toString();
									//根据key找到所有value进行聚合
									List<String> values = valuesMap.get(type);
									if(values!=null && values.size()>0){
										FiltersAggregationBuilder aggregation =  AggregationBuilders.filters(type);
										for (String v : values) {
											aggregation.filter(v, QueryBuilders.termQuery("mutil.items.value", v));
										}
										requestBuilder.addAggregation(aggregation);
									}
								}
							}
						}
					}
				}else if (filter.getPropetyName().equals("tagsName.tagName")){
					
					String currentDate = DateUtil.getUTCTimeStr();
					String pattern = "yyyy-MM-dd HH:mm";
					BoolQueryBuilder bool = QueryBuilders.boolQuery();
					bool.must(QueryBuilders.rangeQuery("promotionPrice.beginDate").format(pattern).lte(currentDate))
						.must(QueryBuilders.rangeQuery("promotionPrice.endDate").format(pattern).gte(currentDate));
					
					FiltersAggregationBuilder aggregation =  AggregationBuilders.filters("tagsName.tagName");
					aggregation.filter("onSale", bool);
					aggregation.filter("freeshipping",QueryBuilders.termsQuery(filter.getPropetyName(),"freeshipping","allfreeshipping"));
					requestBuilder.addAggregation(aggregation);
					
					
//					FilterAggregationBuilder filterBuilder = AggregationBuilders.filter("onSale").filter(bool);
//					requestBuilder.addAggregation(filterBuilder);
//					
//					//tagsName标签
//					TermsBuilder builder = AggregationBuilders.terms(filter.getPropetyName()).field(filter.getPropetyName()).order(Terms.Order.term(true)).size(2000);
//					requestBuilder.addAggregation(builder);

					
				}else{
					TermsBuilder builder = AggregationBuilders.terms(filter.getPropetyName()).field(filter.getPropetyName()).order(Terms.Order.term(true)).size(2000);
					requestBuilder.addAggregation(builder);
				}
			}
		}
		
		
		
		for (RangeAggregation rangeAggregation : aggs) {
			//价格聚合
			Double from = rangeAggregation.getFrom()!=null?rangeAggregation.getFrom():null;
			Double to = rangeAggregation.getTo()!=null?rangeAggregation.getTo():null;
			String key = rangeAggregation.getAliasName();
			RangeBuilder builder = AggregationBuilders.range(key).field(rangeAggregation.getName());
			if(from!=null && to!=null){
				builder.addRange(key,from.doubleValue(),to.doubleValue());
			}else if(from!=null &&( to==null || to==0)){
				builder.addUnboundedFrom(key, from);
			}
			
			requestBuilder.addAggregation(builder);
		}
		
		return requestBuilder;
	}
}

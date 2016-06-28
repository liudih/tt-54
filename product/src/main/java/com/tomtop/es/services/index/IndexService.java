package com.tomtop.es.services.index;

import java.util.List;
import java.util.Map;

import com.tomtop.es.entity.IndexEntity;

/**
 * 索引管理
 * @author ztiny
 * @Date 2015-12-19
 */
public interface IndexService {

	/**
	 * 批量添加索引
	 * @param prods
	 * @return
	 */
	public String insertBlukIndex(String indexName, String indexType,List<IndexEntity> prods);
	
	/**
	 * 单个添加索引
	 * @param 索引名称
	 * @param 索引类型
	 * @param prod 产品
	 * @return
	 */
	public String insertIndex(String indexName,String indexType,IndexEntity product);
	
	/**
	 * 删除索引
	 * @param indexName 索引名称
	 * @param indexType 索引类型
	 * @param map	参数<cloumn_name,column_value>
	 * @return
	 */
	public String deleteIndex(String indexName,String indexType,Map<String,Object> map);
	
	/**
	 * 删除索引
	 * @param indexName 索引名称
	 * @param indexType 索引类型
	 * @param listingids List<参数<cloumn_name,column_value>
	 * @return
	 */
	public String deleteIndex(String indexName,String indexType,List<Map<String,Object>> listingids);
	
	public String deleteBlukIndex();
	
	/**
	 *  更新索引
	 * @param indexName	索引名称
	 * @param indexType	索引类型
	 * @param IndexEntity 产品 
	 * @return
	 */
	public String updateIndex(String indexName,String indexType,IndexEntity index);
	
	public String updateBlukIndex();
	
	/**
	 * 索引局部更新
	 * 
	 * @param indexName 索引名
	 * @param indexType 索引类型
	 * @param listingId 索引ID
	 * @param script script脚本
	 */
	public void update(String indexName, String indexType, String listingId,String script);
}

package com.tomtop.es.services.impl.index;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtop.es.BaseClient;
import com.tomtop.es.common.Config;
import com.tomtop.es.entity.IndexEntity;
import com.tomtop.es.services.index.ISearchIndex;
import com.tomtop.es.services.index.IndexService;

/**
 * 索引管理
 * 
 * @author ztiny
 * @Date 2015-12-19
 */
@Component
public class IndexServiceImpl implements IndexService {

	@Resource(name = "searchIndexImpl")
	private ISearchIndex searchIndexImpl;

	private Logger log = Logger.getLogger(IndexServiceImpl.class);

	@Autowired
	private BaseClient esClient;
	
	/**
	 * 批量添加索引
	 * 
	 * @param prods
	 * @return
	 */
	@Override
	public String insertBlukIndex(String indexName, String indexType,List<IndexEntity> prods) {
		String result = "200";
		try{
			long begin = System.currentTimeMillis();
			Client client = esClient.getIndexClient(indexName, indexType);
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			for (IndexEntity indexEntity : prods) {
				
				String jsonProduct = new ObjectMapper().writeValueAsString(indexEntity);
				IndexRequestBuilder builder = client.prepareIndex(indexName, indexType).setId(indexEntity.getListingId()).setSource(jsonProduct);
				bulkRequest.add(builder);
			}
			BulkResponse bulkResponse = bulkRequest.get();
			if (bulkResponse.hasFailures()) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File(Config.getValue("error.log.path")+"create_index_error.log"), bulkResponse.buildFailureMessage(),true);
				log.error(bulkResponse.buildFailureMessage());
				result = "500";
			}
			long end = System.currentTimeMillis();
			log.info("批量建("+indexName+")索引消耗的时间是："+(end-begin)+" 毫秒");
			
		}catch(Exception ex){
			result = "500";
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 单个添加索引
	 * 
	 * @param 索引名称
	 * @param 索引类型
	 * @param prod
	 *            产品
	 * @return
	 */
	@Override
	public String insertIndex(String indexName, String indexType,IndexEntity product) {
		String result = "200";
		try {
			Client client = esClient.getIndexClient(indexName, indexType);
			String jsonProduct = new ObjectMapper().writeValueAsString(product);
			IndexResponse response = client.prepareIndex(indexName, indexType).setId(product.getListingId()).setSource(jsonProduct).get();
			response.getShardInfo().getSuccessful();
		} catch (Exception ex) {
			
			result = "500";
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除索引
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 * @param map
	 *            参数<cloumn_name,column_value>
	 * @return
	 */
	@Override
	public String deleteIndex(String indexName, String indexType,Map<String, Object> map) {
		String result = "200";

		Client client = esClient.getIndexClient(indexName, indexType);
		try {
			List<String> ids = searchIndexImpl.search(indexName, indexType, map);
			for (String id : ids) {
				DeleteResponse response = client.prepareDelete(indexName,indexType, id).get();
				response.getShardInfo().getSuccessful();
			}
		} catch (Exception ex) {
			result = "500";
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除索引
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 * @param listingids
	 *            List<参数<cloumn_name,column_value>>
	 * @return
	 */
	@Override
	public String deleteIndex(String indexName, String indexType,
			List<Map<String, Object>> listingids) {
		// Client client = BaseClient.getClient();
		// BulkRequestBuilder bulkRequest = client.prepareBulk();
		return null;
	}

	@Override
	public String deleteBlukIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateIndex(String indexName, String indexType,IndexEntity index) {
		String result = "200";
		try {
			Client client = esClient.getIndexClient(indexName, indexType);
			String source = JSONObject.toJSONString(index);
			Map<String, Object> listingMap = new HashMap<String, Object>();
			listingMap.put("listingId", index.getListingId());
			listingMap.put("mutil.languageId", index.getMutil().getLanguageId());
			List<String> ids = searchIndexImpl.search(indexName, indexType,listingMap);
			for (String id : ids) {
				UpdateRequest updateRequest = new UpdateRequest(indexName,indexType, id);
				updateRequest.doc(source);
				client.update(updateRequest).get();
			}
		} catch (Exception e) {
			result = "500";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 索引局部更新
	 * 
	 * @param indexName 索引名
	 * @param indexType 索引类型
	 * @param listingId 索引ID
	 * @param script script脚本
	 * 
	 * 例如：将counter的值加4
	 * curl -XPOST 'localhost:9200/test/type1/1/_update' -d '{
	 *	    "script" : {
	 *	        "inline": "ctx._source.counter += count",
	 *	        "params" : {
	 *	            "count" : 4
	 *	        }
	 *	    }
	 *	}'
	 */
	public void update(String indexName, String indexType, String listingId,
			String script) {
		Client client = esClient.getIndexClient(indexName, indexType);
		UpdateRequestBuilder request = client.prepareUpdate(indexName,indexType, listingId);
		UpdateResponse res = request.setScript(new Script(script)).get();
		log.debug("Index Updated: " + res.getId());
	}

	@Override
	public String updateBlukIndex() {
		// TODO Auto-generated method stub
		return null;
	}

}

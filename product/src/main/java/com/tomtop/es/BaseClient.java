package com.tomtop.es;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tomtop.es.entity.Constant;

/**
 * 获取查询、创建索引的客户端
 * 
 * @author ztiny
 * @Date 2015-12-19
 */
@Service
public class BaseClient {
	private static final Logger logger = Logger.getLogger(BaseClient.class);

	@Autowired
	@Qualifier("indexClient")
	private TransportClient indexClient;

	@Autowired
	@Qualifier("transportClient")
	private TransportClient transportClient;

	/**
	 * 获取elastic search 客户端,后续参数会从配置文件读取,单机可用
	 * 
	 * @param ip
	 * @return
	 */
	public Client getClient(String ip) {
		Client client = null;
		try {
			client = TransportClient
					.builder()
					.build()
					.addTransportAddress(
							new InetSocketTransportAddress(InetAddress
									.getByName(ip), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return client;
	}

	public Client getSearchClient() {
		return transportClient;
	}

	/**
	 * 获取客户端
	 * 
	 * @param indexName
	 * @param indexType
	 * @param client
	 * @return
	 */
	public Client getIndexClient(String indexName, String indexType) {
		try {
			Assert.notNull(indexName, "indexName为空");
			Assert.notNull(indexType, "indexType为空");
			String indexKey = indexName + "_" + indexType;
			// 判断索引映射文件是否已经存在
			Boolean exists = Constant.indexMappingFlagCache.get(indexKey);
			if (exists == null || !exists) {
				String path = BaseClient.class.getClassLoader().getResource("")
						.toURI().getPath()
						+ "/templates/mapping_product.json";
				String mapping = insertMapping(path);
				exists = indexClient.admin().indices().prepareExists(indexName)
						.execute().actionGet().isExists();
				if (!exists) {
					indexClient.admin().indices().prepareCreate(indexName)
							.execute().actionGet();
					indexClient.admin().indices().preparePutMapping(indexName)
							.setType(indexType).setSource(mapping).execute()
							.actionGet();
				}
				Constant.indexMappingFlagCache.put(indexKey, exists);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return indexClient;
	}

	/**
	 * 文件名称
	 * 
	 * @param filepath
	 * @return
	 */
	public static String insertMapping(String path) {
		String mapping = null;
		try {
			File file = new File(path);
			mapping = org.apache.commons.io.FileUtils.readFileToString(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping;
	}
}

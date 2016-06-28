package com.tomtop.es.services;

import java.util.Map;

public interface IBeforeIndexService {

	public static final String LANG_GROOVY = "groovy";
	
	/**
	 * 创建索引
	 * 
	 * @param product
	 *            json字符串
	 */
	public Map<String,Object> insert(String product);
	
	/**
	 * 批量创建索引
	 * @param product
	 * @return
	 */
	public Map<String,Object> insertBulk(String product);
	/**
	 * 全量修改索引
	 * 
	 * @param product
	 *            json字符串
	 * @return
	 */
	public Map<String, Object> updateIndex(String product);

	/**
	 * 删除索引
	 * 
	 * @param type
	 *            (状态为1和2，1代表根据根据lingingId删除,2代表根据sku删除)
	 * @param language
	 *            语言,国家域名缩写
	 * @param map
	 *            <删除的健,删除的值>
	 * @return
	 */
	public Map<String, Object> deleteIndex(int type, String language,
			Map<String, Object> map);

	/**
	 * 局部修改索引
	 * 
	 * @param language
	 *            语言
	 * @param listingid
	 * @param 修改属性的脚本
	 * @return
	 */
	public Map<String, Object> updateIndexPart(String language,
			String listingid, String script);

	/**
	 * 全量修改索引
	 * 
	 * @author lijun
	 * @param map
	 *            <属性路径,属性值>
	 * @return
	 */
	public boolean updateIndex(String language, String listingid,
			Map<String, Object> map);

	public boolean updateIndexByScript(String language, String listingid,
			Map<String, Object> map);
}

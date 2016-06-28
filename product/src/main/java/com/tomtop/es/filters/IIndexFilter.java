package com.tomtop.es.filters;

import java.util.Map;

/**
 * 建索引过滤器
 * 
 * @author lijuns
 *
 */
public interface IIndexFilter {

	public static final String ITEMS_KEY = "mutil.items";
	public static final String PRODUCT_TYPES_KEY = "mutil.productTypes";
	public static final String MUTIL_KEY = "mutil";
	
	/**
	 * 优先级
	 * 
	 * @return
	 */
	public int getPriority();

	/**
	 * 做特殊处理
	 * 
	 * @param attributes
	 * @return
	 */
	public void handle(int lang, Map<String, Object> attributes);
}

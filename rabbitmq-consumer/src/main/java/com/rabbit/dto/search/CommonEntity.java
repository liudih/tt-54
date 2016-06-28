package com.rabbit.dto.search;

/**
 * 常量实体类
 * @author ztiny
 * @Date 2015-12-19
 */
public class CommonEntity {

	 //用于接口调用的状态码
	 public enum returnStatus {
		 /** 内部错误 */
		INNER_ERROR {
			public String getName() {
				return "500";
			}
		},
		/** 某个字段可能为空 */
		COLUMN_ISNULL {
			public String getName() {
				return "100";
			}
		},
		/**索引建立成功  */
		SUCESS {
			public String getName() {
				return "200";
			}
		};
		 public abstract String getName();  
	 }
	 
	 public static final String ES_CONFIG_PATH = "/config.properties";
	 //索引名称前缀
	 public static final String ES_INDEX_PREFIX="index_prodcut_";
	 //产品索引类型
	 public static final String ES_INDEX_PRODUCT_TYPE = "product";
	 //配置文件索引域名站点的健对应的值
	 public static final String ES_CONFIG_PRODUCT_INDEX_ALL_KEY="product.all";
	 
}

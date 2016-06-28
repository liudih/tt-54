package com.tomtop.es.common;

import java.util.Properties;

import com.tomtop.es.entity.Constant;

/**
 * config.properties配置文件里面的配置信息
 * 
 * @author lijun
 *
 */
public class Config {

	static Properties config;
	
	static{
		config = PropertyReader.getProperties(Constant.ES_CONFIG_PATH);
	}
	
	
	public static String getValue(String key){
		return config.getProperty(key);
	}
}

package services;

import java.util.List;
import java.util.Map;

/**
 * 客户端去服务端配置服务
 * 
 * @author lijun
 *
 */
public interface IConfigService {
	/**
	 * 获取单个配置
	 * 
	 * @param key
	 * @return
	 */
	public String getConfig(String key);

	/**
	 * 获取多个配置
	 * 
	 * @param keys
	 * @return
	 */
	public Map<String, String> getConfig(List<String> keys);
}

package factory.common;

/**
 * 服务请求工厂接口
 * 
 * @author lijun
 *
 */
public interface ServiceInvokeFactory {
	/**
	 * 获取服务
	 * 
	 * @param url
	 * @param type
	 * @return maybe return null
	 */
	public <T> T getService(String url,Class<T> type);
}

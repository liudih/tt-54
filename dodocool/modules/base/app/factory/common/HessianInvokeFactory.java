package factory.common;

import play.Logger;

import com.caucho.hessian.client.HessianProxyFactory;

public class HessianInvokeFactory implements ServiceInvokeFactory {

	@Override
	public <T> T getService(String url, Class<T> type) {
		if (url == null || url.length() == 0) {
			return null;
		}

		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setOverloadEnabled(true);
		// 超时时间20s
		factory.setReadTimeout(20 * 1000);
		T service = null;
		try {
			service = (T) factory.create(type, url);
		} catch (Exception e) {
			Logger.error("get service:{} failed", type.getClass().getName(), e);
		}
		return service;
	}
}

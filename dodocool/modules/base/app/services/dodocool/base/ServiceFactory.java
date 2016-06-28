package services.dodocool.base;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;

public class ServiceFactory {

	/**
	 * 通过传入接口类型，和服务名称，获取接口服务
	 */
	@SuppressWarnings("unchecked")
	public <T> T getIService(Class<T> T, String serviceName)
			throws InstantiationException, IllegalAccessException,
			MalformedURLException, ClassNotFoundException {

		HessianProxyFactory factory = new HessianProxyFactory();
		T t = (T) factory.create("http://localhost:9000/common/hs/"
				+ serviceName);
		return t;
	}
}

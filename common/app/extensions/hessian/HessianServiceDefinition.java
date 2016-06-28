package extensions.hessian;

import extensions.InjectorInstance;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HessianServiceDefinition {

	final String path;
	final Class serviceClass;
	final Class implClass;

	public <T> HessianServiceDefinition(String path, Class<T> serviceClass,
			Class<? extends T> implClass) {
		super();
		this.path = path;
		this.serviceClass = serviceClass;
		this.implClass = implClass;
	}

	public String getPath() {
		return path;
	}

	public Class getServiceClass() {
		return serviceClass;
	}

	public Object getServiceObject() {
		return InjectorInstance.getInstance(implClass);
	}

}

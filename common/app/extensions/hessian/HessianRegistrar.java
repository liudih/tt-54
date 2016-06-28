package extensions.hessian;

import java.util.List;

import com.google.common.collect.Lists;

public class HessianRegistrar {

	List<HessianServiceDefinition> serviceDefs = Lists.newArrayList();

	public <T> void publishService(String path, Class<T> intf,
			Class<? extends T> implClass) {
		serviceDefs.add(new HessianServiceDefinition(path, intf, implClass));
	}

	public List<HessianServiceDefinition> getServiceDefinitions() {
		return serviceDefs;
	}

}

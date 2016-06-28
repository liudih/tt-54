package extensions.hessian;

import extensions.IExtensionPoint;

public interface HessianServiceExtension extends IExtensionPoint {

	void registerRemoteService(HessianRegistrar reg);

}

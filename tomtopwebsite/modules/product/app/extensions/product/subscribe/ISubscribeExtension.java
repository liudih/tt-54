package extensions.product.subscribe;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ISubscribeExtension extends IExtensionPoint {

	void registerSubscribeProvider(Multibinder<ISubscribeProvider> binder);

}

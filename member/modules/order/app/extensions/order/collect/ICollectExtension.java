package extensions.order.collect;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ICollectExtension extends IExtensionPoint {

	void registerCollectProvider(Multibinder<ICollectProvider> binder);

}

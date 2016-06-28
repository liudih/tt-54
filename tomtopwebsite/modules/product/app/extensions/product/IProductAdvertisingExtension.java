package extensions.product;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IProductAdvertisingExtension extends IExtensionPoint {
	public void registerProductAdvertisingFragment(
			Multibinder<IProductAdvertisingProvider> plugins);
}

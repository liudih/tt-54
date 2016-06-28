package extensions.order;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IOrderExtrasExtension extends IExtensionPoint {

	/**
	 * 登记 IOrderExtrasProvider 扩展
	 * 
	 * @param extrasProvider
	 */
	void registerOrderExtrasProvider(
			Multibinder<IOrderExtrasProvider> extrasProvider);

}

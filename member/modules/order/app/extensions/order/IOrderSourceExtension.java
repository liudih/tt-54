package extensions.order;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

/**
 * 用于订单来源判断
 * 
 * @author kmtong
 *
 */
public interface IOrderSourceExtension extends IExtensionPoint {

	public void registerOrderSourceProvider(
			Multibinder<IOrderSourceProvider> plugins);
}

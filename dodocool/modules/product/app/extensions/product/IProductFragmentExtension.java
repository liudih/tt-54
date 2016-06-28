package extensions.product;

import services.dodocool.product.IProductFragmentPlugin;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IProductFragmentExtension extends IExtensionPoint {

	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins);

}

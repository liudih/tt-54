package extensions.cart;

import services.cart.ICartFragmentPlugin;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ICartFragmentExtension extends IExtensionPoint {
	public void registerCartFragment(Multibinder<ICartFragmentPlugin> plugins);
}

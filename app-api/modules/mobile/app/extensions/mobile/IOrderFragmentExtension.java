package extensions.mobile;

import plugins.IOrderFragmentPlugin;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IOrderFragmentExtension extends IExtensionPoint {

	public void registerOrdersFragment(Multibinder<IOrderFragmentPlugin> plugins);
}

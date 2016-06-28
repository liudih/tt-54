package extensions.interaction.share;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IShareExtension extends IExtensionPoint {

	void registerShareProvider(Multibinder<IShareProvider> binder);

}

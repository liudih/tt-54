package extensions.interaction.share;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;
import extensions.base.IShareProvider;

public interface IShareExtension extends IExtensionPoint {

	void registerShareProvider(Multibinder<IShareProvider> binder);

}

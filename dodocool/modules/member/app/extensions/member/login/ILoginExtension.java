package extensions.member.login;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ILoginExtension extends IExtensionPoint {

	void registerLoginProvider(Multibinder<ILoginprovider> binder);

}

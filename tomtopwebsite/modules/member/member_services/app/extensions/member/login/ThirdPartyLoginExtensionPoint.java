package extensions.member.login;

import services.member.login.ILoginOther;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ThirdPartyLoginExtensionPoint extends IExtensionPoint {

	void registerThirdPartyLoginProvider(Multibinder<IThirdPartyLoginService> binder);
	
	void registerThirdPartyLoginOther(Multibinder<ILoginOther> binder);
	

}

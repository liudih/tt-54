package extension.point;


import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IExtensionSignin extends IExtensionPoint {

	public void registerSignin(
			Multibinder<ISigninProvider> plugins);
}

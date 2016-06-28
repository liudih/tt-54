package extensions.member.login;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ILoginProcessExtension extends IExtensionPoint {

	public void registerLoginProcess(
			Multibinder<ILoginProcess> provider);

}

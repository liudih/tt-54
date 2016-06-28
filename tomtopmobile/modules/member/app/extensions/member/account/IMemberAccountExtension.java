package extensions.member.account;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IMemberAccountExtension extends IExtensionPoint {

	void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders);

}

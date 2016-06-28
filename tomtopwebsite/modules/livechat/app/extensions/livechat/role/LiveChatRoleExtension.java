package extensions.livechat.role;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface LiveChatRoleExtension extends IExtensionPoint {

	void registerRoles(Multibinder<EnquiryRoleProvider> enquiryRoleProviders,
			Multibinder<SupportRoleProvider> supportRoleProviders);

}

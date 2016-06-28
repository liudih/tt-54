package extensions.livechat.role;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface LiveChatOnDutyCustomerServiceExtension extends IExtensionPoint {
	void registerDutyCustomerService(Multibinder<ILiveChatOnDutyCustomerServiceProvider> csBinder);
}

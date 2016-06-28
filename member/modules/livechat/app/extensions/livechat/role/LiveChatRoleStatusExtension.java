package extensions.livechat.role;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

/**
 * 判断当前 用户状态
 * 
 * @author Administrator
 *
 */
public interface LiveChatRoleStatusExtension extends IExtensionPoint {

	void registerRoleStatus(
			Multibinder<LiveChatRoleStatusProvider> binder);
}

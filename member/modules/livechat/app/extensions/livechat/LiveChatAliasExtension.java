package extensions.livechat;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface LiveChatAliasExtension extends IExtensionPoint {

	public void registerAliasResolver(Multibinder<LiveChatAliasResolver> binder);

}

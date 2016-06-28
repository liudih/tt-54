package extensions.livechat.topic;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ChatTopicExtension extends IExtensionPoint {

	void registerChatTopics(Multibinder<ChatTopicProvider> topics);

}

package extensions.livechat.topic;

import java.util.List;

public interface ChatTopicProvider {

	/**
	 * Return all available topics to be listed in UI as call destinations.
	 * 
	 * @return
	 */
	List<ChatTopic> getTopics(int langaugeId);
}

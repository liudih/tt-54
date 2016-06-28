package extensions.livechat.topic;

import java.util.List;

public interface IWelcomeSentenceProvider {

	List<WelcomeSentence> getWelcomeSentence(int langaugeId);
}

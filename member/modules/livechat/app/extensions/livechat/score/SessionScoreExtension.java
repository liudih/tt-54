package extensions.livechat.score;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface SessionScoreExtension extends IExtensionPoint {
	void registerSessionScores(
			Multibinder<SessionScoreQuestionProvider> questionBind);
}

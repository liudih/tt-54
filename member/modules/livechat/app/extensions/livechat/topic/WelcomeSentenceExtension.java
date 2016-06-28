package extensions.livechat.topic;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface WelcomeSentenceExtension extends IExtensionPoint {

	void registerWelcomeSentence(Multibinder<IWelcomeSentenceProvider> ws);

}

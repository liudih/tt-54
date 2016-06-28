package extensions.messaging;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.ModuleSupport;
import extensions.event.IEventExtension;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.runtime.IApplication;

public class MessagingModule extends ModuleSupport implements
		IMemberAccountExtension, IEventExtension {

	@Override
	public Module getModule(IApplication arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerListener(EventBus arg0, Injector arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders) {
		fragmentProviders.addBinding().to(MessageMenuProvider.class);
		
	}

}

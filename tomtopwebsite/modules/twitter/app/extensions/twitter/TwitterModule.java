package extensions.twitter;

import services.base.template.ITemplateFragmentProvider;

import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.ModuleSupport;
import extensions.base.template.ITemplateExtension;
import extensions.interaction.share.IShareExtension;
import extensions.interaction.share.IShareProvider;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProvider;
import extensions.runtime.IApplication;
import extensions.twitter.login.TwitterLoginProvider;

public class TwitterModule extends ModuleSupport implements ILoginExtension,ITemplateExtension,IShareExtension {
	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void registerLoginProvider(Multibinder<ILoginProvider> binder) {
		binder.addBinding().to(TwitterLoginProvider.class);
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		
		
	}

	@Override
	public void registerShareProvider(Multibinder<IShareProvider> binder) {
		// TODO Auto-generated method stub
		
	}
	
	

}

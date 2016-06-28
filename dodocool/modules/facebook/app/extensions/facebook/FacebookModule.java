package extensions.facebook;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.facebook.login.FacebookLoginProvider;
import extensions.member.MemberModule;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginprovider;
import extensions.runtime.IApplication;

public class FacebookModule extends ModuleSupport implements ILoginExtension {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(MemberModule.class);
	}

	@Override
	public void registerLoginProvider(Multibinder<ILoginprovider> binder) {
		binder.addBinding().to(FacebookLoginProvider.class);
	}

}

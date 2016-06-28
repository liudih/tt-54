package extensions.facebook;

import java.util.List;
import java.util.Set;

import services.base.template.ITemplateFragmentProvider;
import services.member.login.ILoginOther;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.template.ITemplateExtension;
import extensions.facebook.like.FacebookLikeProvider;
import extensions.facebook.login.FacebookLogin;
import extensions.facebook.login.FacebookLoginProvider;
import extensions.facebook.share.FacebookShareProvider;
import extensions.interaction.InteractionModule;
import extensions.interaction.share.IShareExtension;
import extensions.interaction.share.IShareProvider;
import extensions.member.MemberModule;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProvider;
import extensions.member.login.IThirdPartyLoginService;
import extensions.member.login.ThirdPartyLoginExtensionPoint;
import extensions.runtime.IApplication;

public class FacebookModule extends ModuleSupport implements ILoginExtension,
		ITemplateExtension, IShareExtension, ThirdPartyLoginExtensionPoint {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(MemberModule.class, InteractionModule.class);
	}

	@Override
	public void registerLoginProvider(Multibinder<ILoginProvider> binder) {
		binder.addBinding().to(FacebookLoginProvider.class);
	}

	@Override
	public void registerShareProvider(Multibinder<IShareProvider> binder) {
		binder.addBinding().to(FacebookShareProvider.class);
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(FacebookLikeProvider.class);
	}

	@Override
	public void registerThirdPartyLoginProvider(
			Multibinder<IThirdPartyLoginService> binder) {
		binder.addBinding().to(FaceThridPartyLoginService.class);
	}

	@Override
	public Module getModule(IApplication arg0) {
		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		
	}

	@Override
	public void registerThirdPartyLoginOther(Multibinder<ILoginOther> binder) {
		binder.addBinding().to(FacebookLogin.class);
	}

}

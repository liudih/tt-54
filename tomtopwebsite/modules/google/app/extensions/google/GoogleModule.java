package extensions.google;

import java.util.List;
import java.util.Set;

import services.member.login.ILoginOther;

import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.google.login.GoogleLogin;
import extensions.google.login.GoogleLoginProvider;
import extensions.google.merchants.GoogleProductMerchantsService;
import extensions.google.share.GoogleShareProvider;
import extensions.interaction.share.IShareExtension;
import extensions.interaction.share.IShareProvider;
import extensions.member.MemberModule;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProvider;
import extensions.member.login.IThirdPartyLoginService;
import extensions.member.login.ThirdPartyLoginExtensionPoint;
import extensions.product.IProductMerchantsService;
import extensions.runtime.IApplication;

public class GoogleModule extends ModuleSupport implements ILoginExtension,
		IShareExtension, IFilterExtension, ThirdPartyLoginExtensionPoint {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(MemberModule.class);
	}

	@Override
	public void registerLoginProvider(Multibinder<ILoginProvider> binder) {
		binder.addBinding().to(GoogleLoginProvider.class);
	}

	@Override
	public void registerShareProvider(Multibinder<IShareProvider> binder) {
		binder.addBinding().to(GoogleShareProvider.class);
	}

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(GoogleTagManagerScriptAddition.class);
	}

	@Override
	public void registerThirdPartyLoginProvider(
			Multibinder<IThirdPartyLoginService> binder) {
		binder.addBinding().to(GoogleThridPartyLoginService.class);
	}
	

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		binder.bind(IProductMerchantsService.class).to(GoogleProductMerchantsService.class);
	}

	@Override
	public Module getModule(IApplication arg0) {
		return null;
	}

	@Override
	public void registerThirdPartyLoginOther(Multibinder<ILoginOther> binder) {
		binder.addBinding().to(GoogleLogin.class);
	}
}

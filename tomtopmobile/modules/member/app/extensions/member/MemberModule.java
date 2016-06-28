package extensions.member;

import java.util.List;

import services.base.template.ITemplateFragmentProvider;

import com.google.common.collect.FluentIterable;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.ITemplateExtension;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.impl.AccountHomePointFragmentProvider;
import extensions.member.account.impl.MemberCenterQuickMenuProvider;
import extensions.member.account.impl.MemberResetPasswordProvider;
import extensions.member.account.impl.MyprofileMenuProvider;
import extensions.member.account.impl.SignOutQuickMenuProvider;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProvider;
import extensions.runtime.IApplication;

public class MemberModule extends ModuleSupport implements ITemplateExtension,
		IMemberAccountExtension {

	@Override
	public Module getModule(IApplication app) {
		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {

		final Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders = Multibinder
				.newSetBinder(binder, IMemberAccountHomeFragmentProvider.class);
		for (IMemberAccountExtension e : filterModules(modules,
				IMemberAccountExtension.class)) {
			e.registerMemberAccountRelatedProviders(fragmentProviders);
		}

		final Multibinder<ILoginProvider> loginProviderBinder = Multibinder
				.newSetBinder(binder, ILoginProvider.class);

		FluentIterable.from(modules).filter(ILoginExtension.class)
				.forEach(e -> {
					e.registerLoginProvider(loginProviderBinder);
				});

	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(MemberNavigationBarRegion.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders) {
		fragmentProviders.addBinding().to(SignOutQuickMenuProvider.class);
		fragmentProviders.addBinding().to(MemberCenterQuickMenuProvider.class);
		fragmentProviders.addBinding().to(
				AccountHomePointFragmentProvider.class);
		fragmentProviders.addBinding().to(MyprofileMenuProvider.class);
		fragmentProviders.addBinding().to(MemberResetPasswordProvider.class);
	}
}

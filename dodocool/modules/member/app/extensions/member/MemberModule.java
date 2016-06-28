package extensions.member;

import java.util.List;

import services.dodocool.base.template.ITemplateFragmentProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.template.TemplateFragmentExtension;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.member.account.MemberEditMenuProvider;
import extensions.member.account.MemberHomeMenuProvider;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginprovider;
import extensions.member.template.MemberHeaderTemplate;
import extensions.runtime.IApplication;

public class MemberModule extends ModuleSupport implements
		TemplateFragmentExtension, IMemberAccountExtension {

	@Override
	public Module getModule(IApplication arg0) {
		return new AbstractModule() {
			@Override
			protected void configure() {

			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<IMemberAccountMenuProvider> menuProviders = Multibinder
				.newSetBinder(binder, IMemberAccountMenuProvider.class);
		final Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders = Multibinder
				.newSetBinder(binder, IMemberAccountHomeFragmentProvider.class);
		final Multibinder<IMemberQuickMenuProvider> quickMenuProviders = Multibinder
				.newSetBinder(binder, IMemberQuickMenuProvider.class);
		for (IMemberAccountExtension e : filterModules(modules,
				IMemberAccountExtension.class)) {
			e.registerMemberAccountRelatedProviders(menuProviders,
					fragmentProviders, quickMenuProviders);
		}
		
		final Multibinder<ILoginprovider> loginProviders = Multibinder
				.newSetBinder(binder, ILoginprovider.class);
		for (ILoginExtension e : filterModules(modules, ILoginExtension.class)) {
			e.registerLoginProvider(loginProviders);
		}
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(MemberHeaderTemplate.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {
		menuProviders.addBinding().to(MemberHomeMenuProvider.class);
		menuProviders.addBinding().to(MemberEditMenuProvider.class);
	}

}

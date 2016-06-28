package extensions.base;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import play.Configuration;
import play.Logger;
import play.Play;
import services.dodocool.base.template.ITemplateFragmentProvider;
import services.dodocool.cms.IndexCmsFragmentProvider;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.template.TemplateFragmentExtension;
import extensions.common.CommonModule;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.runtime.IApplication;
import factory.common.HessianInvokeFactory;
import factory.common.ServiceInvokeFactory;

public class BaseModule extends ModuleSupport implements
		TemplateFragmentExtension ,IFilterExtension{

	private void binderHessianService(Binder binder) {
		Properties p = new Properties();
		try {
			ServiceInvokeFactory factory = new HessianInvokeFactory();
			p.load(Play.application().classloader()
					.getResourceAsStream("HessianService.properties"));
			// hessian.service
			Configuration config = Play.application().configuration()
					.getConfig("hessian");
			String baseUrl = config.getString("service");

			FluentIterable
					.from(p.keySet())
					.filter(k -> k.toString().startsWith("service."))
					.forEach(
							k -> {
								Logger.debug(k.toString());
								Class c = this.loadClass(p.getProperty(k
										.toString()));
								if (c != null) {
									String[] key = k.toString().split("\\.");
									if (key != null && key.length > 1) {
										String url = baseUrl + key[1];
										Logger.debug(url);
										Object servictObject = factory
												.getService(url, c);
										if (servictObject != null) {
											binder.bind(c).toInstance(
													servictObject);
										}
										Logger.debug("binder {} succeed",
												c.getName());
									}
								}
							});
			;
		} catch (IOException e) {
			Logger.debug("Loading file : modules.properties error", e);
		}
	}

	private Class loadClass(String clazz) {
		try {
			return Class.forName(clazz);
		} catch (Exception e) {
			Logger.error("hessian service class error: " + clazz, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<HtmlRenderHook> hooks = Multibinder.newSetBinder(
				binder, HtmlRenderHook.class);
		for (IHtmlHookExtension e : filterModules(modules,
				IHtmlHookExtension.class)) {
			e.registerHtmlHook(hooks);
		}

		final Multibinder<ITemplateFragmentProvider> signinProviders = Multibinder
				.newSetBinder(binder, ITemplateFragmentProvider.class);
		for (TemplateFragmentExtension e : filterModules(modules,
				TemplateFragmentExtension.class)) {
			e.registerTemplateProviders(signinProviders);
		}

		this.binderHessianService(binder);
	}

	@Override
	public Module getModule(IApplication arg0) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				// TODO Auto-generated method stub
			}
		};
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(IndexCmsFragmentProvider.class);
	}

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(GoogleTagManagerScriptAddition.class);
	}

}

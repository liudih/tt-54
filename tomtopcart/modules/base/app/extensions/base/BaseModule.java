package extensions.base;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import play.Logger;
import play.Play;
import play.libs.Json;
import services.cart.base.template.ITemplateFragmentProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.tracking.GoogleTagManagerScriptAddition;
import extensions.common.CommonModule;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.runtime.IApplication;
import factory.common.HessianInvokeFactory;
import factory.common.ServiceInvokeFactory;

public class BaseModule extends ModuleSupport implements ITemplateExtension, IFilterExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class);
	}

	@Override
	public Module getModule(IApplication app) {
		return null;

	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		this.binderHessianService(binder);

//		Multibinder<ITemplateFragmentProvider> b = Multibinder
//		.newSetBinder(binder, ITemplateFragmentProvider.class);
//		
//		FluentIterable.from(modules).filter(
//				m -> ITemplateExtension.class.isAssignableFrom(m.getClass())).forEach(m ->{
//					ITemplateExtension c = (ITemplateExtension) m;
//					c.registerTemplateProviders(b);
//				});;
				
		final Multibinder<ITemplateFragmentProvider> tfProviders = Multibinder
				.newSetBinder(binder, ITemplateFragmentProvider.class);

		for (ITemplateExtension e : filterModules(modules,
				ITemplateExtension.class)) {
			e.registerTemplateProviders(tfProviders);
		}

	}

	/**
	 * 绑定Hessian服务类
	 * 
	 * @author lijun
	 * @param binder
	 */
	private void binderHessianService(Binder binder) {
		try {
			ServiceInvokeFactory factory = new HessianInvokeFactory();
			JsonNode jsonConfig = Json.parse(Play.application().classloader()
					.getResourceAsStream("HessianService-json.properties"));

			if (jsonConfig.isArray()) {
				Iterator<JsonNode> iterator = jsonConfig.iterator();
				while (iterator.hasNext()) {
					JsonNode node = iterator.next();
					String url = node.findValue("url").asText();
					String serviceRemoteUrl = Play.application()
							.configuration().getString(url);
					JsonNode services = node.findValue("service");
					if (services.isArray()) {
						Iterator<JsonNode> si = services.iterator();
						while (si.hasNext()) {
							JsonNode service = si.next();
							String endPoint = service.findValue("endPoint")
									.asText();
							String cl = service.findValue("class").asText();

							Class c = this.loadClass(cl);
							String serviceUrl = serviceRemoteUrl
									+ endPoint.trim();
							Object servictObject = factory.getService(
									serviceUrl, c);
							if (servictObject != null) {
								binder.bind(c).toInstance(servictObject);
								Logger.debug("binder {} succeed,url : {}", cl,
										serviceUrl);
							} else {
								Logger.debug("binder {} failed", cl);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			Logger.debug("Loading file : HessianService.properties error", e);
		}
	}

	/**
	 * @author lijun
	 * @param clazz
	 * @return
	 */
	private Class loadClass(String clazz) {
		try {
			return Class.forName(clazz);
		} catch (Exception e) {
			Logger.error("hessian service class error: " + clazz, e);
			return null;
		}
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
	}

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(GoogleTagManagerScriptAddition.class);
	}
}

package extensions.base;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mapper.base.CountryMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import play.Logger;
import play.Play;
import play.libs.Json;
import services.base.fragment.CurrencyFragmentProvider;
import services.base.template.ITemplateFragmentProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.common.CommonModule;
import extensions.runtime.IApplication;
import factory.common.HessianInvokeFactory;
import factory.common.ServiceInvokeFactory;

public class BaseModule extends ModuleSupport implements ITemplateExtension,
	MyBatisExtension{

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
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("base", CountryMapper.class);
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
		this.binderHessianService(binder);
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
					.getResourceAsStream("HessianService-json"));
			
			if(jsonConfig.isArray()){
				Iterator<JsonNode> iterator = jsonConfig.iterator();
				while(iterator.hasNext()){
					JsonNode node = iterator.next();
					String url = node.findValue("url").asText();
					String serviceRemoteUrl = Play.application().configuration()
							.getString(url);
					JsonNode services = node.findValue("service");
					if(services.isArray()){
						Iterator<JsonNode> si = services.iterator();
						while(si.hasNext()){
							JsonNode service = si.next();
							String endPoint = service.findValue("endPoint").asText();
							String cl = service.findValue("class").asText();
							
							Class c = this.loadClass(cl);
							String serviceUrl = serviceRemoteUrl + endPoint.trim();
							Object servictObject = factory
									.getService(serviceUrl, c);
							if (servictObject != null) {
								binder.bind(c).toInstance(
										servictObject);
								Logger.debug("binder {} succeed,url : {}",
										cl,serviceUrl);
							} else {
								Logger.debug("binder {} failed",cl);
							}
						}
					}
				}
				
			}
		} catch (Exception e) {
			Logger.debug("Loading file : HessianService-json.properties error", e);
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
		binder.addBinding().to(CurrencyFragmentProvider.class);
	}
}

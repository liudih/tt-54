package extensions.mobile;

import handlers.mobile.LoginHandler;
import handlers.mobile.VisitLogHandler;
import intercepters.IMoneyIntercepter;
import intercepters.JPYMoneyIntercepter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import mapper.AppVersionMapper;
import mapper.ClientErrorLogMapper;
import mapper.ContactMapper;
import mapper.InterestTagMapper;
import mapper.SettingMapper;
import mapper.VisitLogMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import org.redisson.Config;
import org.redisson.MasterSlaveServersConfig;
import org.redisson.Redisson;
import org.redisson.SentinelServersConfig;

import play.Logger;
import play.Play;
import play.libs.Json;
import plugins.IOrderFragmentPlugin;
import plugins.SimpleOrderFragmentPlugin;
import plugins.provider.OrderShippingMethodProvider;
import plugins.renderer.OrderShippingMethodRenderer;
import services.mobile.IMobileSessionService;
import services.mobile.MobileSessionService;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.common.CommonModule;
import extensions.event.IEventExtension;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;
import factory.common.HessianInvokeFactory;
import factory.common.ServiceInvokeFactory;

public class MobileModule extends ModuleSupport implements MyBatisExtension,
		IEventExtension, IOrderFragmentExtension {

	private void binderHessianService(Binder binder) {
		Properties p = new Properties();
		try {
			ServiceInvokeFactory factory = new HessianInvokeFactory();
			JsonNode jsonConfig = Json.parse(Play.application().classloader()
					.getResourceAsStream("HessianServiceJson.properties"));
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
			Logger.debug("Loading file : HessianService-json.properties error",
					e);
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
		final Multibinder<IMoneyIntercepter> moneyIntercepters = Multibinder
				.newSetBinder(binder, IMoneyIntercepter.class);
		moneyIntercepters.addBinding().to(JPYMoneyIntercepter.class);

		this.binderHessianService(binder);
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("mobile", VisitLogMapper.class);
		service.addMapperClass("mobile", ClientErrorLogMapper.class);
		service.addMapperClass("mobile", SettingMapper.class);
		service.addMapperClass("mobile", ContactMapper.class);
		service.addMapperClass("mobile", AppVersionMapper.class);
		service.addMapperClass("mobile", InterestTagMapper.class);
	}

	@Override
	public void registerOrdersFragment(Multibinder<IOrderFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleOrderFragmentPlugin("shipping-method",
						OrderShippingMethodProvider.class,
						OrderShippingMethodRenderer.class));
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(VisitLogHandler.class));
		eventBus.register(injector.getInstance(LoginHandler.class));
	}

	@Override
	public Module getModule(IApplication application) {
		IConfiguration sessionConfig = application.getConfiguration()
				.getConfig("session");
		if (sessionConfig == null) {
			Logger.error("Mobile Redis Configuration not set");
		}
		String type = sessionConfig.getString("server_type");
		Integer databaseNum = sessionConfig.getInt("database", 0);
		List<Object> servers = new LinkedList<Object>();
		try {
			servers.addAll(sessionConfig.getList("server_address"));
		} catch (Exception e) {
			servers.addAll(Arrays.asList(sessionConfig.getString(
					"server_address").split(",")));
		}
		Long timeout = sessionConfig.getLong("timeout", Long.valueOf(7200L));

		Logger.info("Mobile Redis Server Type: {}", type);
		Logger.info("Mobile Redis Server Address: {}", servers);
		Logger.info("Mobile Redis Database: {}", databaseNum);
		Config config = new Config();
		if (type == null || "single".equals(type)) {
			config.useSingleServer().setAddress(servers.get(0).toString())
					.setDatabase(databaseNum);
		} else if ("master_slave".equals(type)) {
			MasterSlaveServersConfig msconfig = config
					.useMasterSlaveConnection();
			boolean masterSet = false;
			for (Object a : servers) {
				if (!masterSet) {
					msconfig.setMasterAddress(a.toString());
					masterSet = true;
				} else {
					msconfig.addSlaveAddress(a.toString());
				}
			}
			msconfig.setDatabase(databaseNum);
		} else if ("sentinel".equals(type)) {
			SentinelServersConfig sconfig = config.useSentinelConnection();
			sconfig.setMasterName("master");
			for (Object a : servers) {
				sconfig.addSentinelAddress(a.toString());
			}
			sconfig.setDatabase(databaseNum);
		} else {
			throw new RuntimeException(
					"Redis config error, session.server_type must be one of ['single', 'master_slave', 'sentinel']");
		}
		Redisson redisson = Redisson.create(config);
		return new AbstractModule() {
			@Override
			protected void configure() {
				IMobileSessionService sessionService = new MobileSessionService(
						redisson, timeout);
				bind(IMobileSessionService.class).toInstance(sessionService);
			}
		};
	}
}

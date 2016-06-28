package extensions.base;

import intercepters.IMoneyIntercepter;
import intercepters.JPYMoneyIntercepter;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.redisson.Config;
import org.redisson.MasterSlaveServersConfig;
import org.redisson.Redisson;
import org.redisson.SentinelServersConfig;

import play.Logger;
import play.Play;
import services.IMoneyService;
import services.base.activity.template.ITemplateFragmentProvider;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.activity.IActivityPrizeProvider;
import extensions.activity.IActivityQualificationProvider;
import extensions.activity.IActivityRuleProvider;
import extensions.activity.IPrizeExtension;
import extensions.activity.IQualificationExtension;
import extensions.activity.IRuleExtension;
import extensions.base.activity.IActivitySessionService;
import extensions.base.activity.RedisActivitySessionService;
import extensions.common.CommonModule;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;
import factory.common.HessianInvokeFactory;
import factory.common.ServiceInvokeFactory;

public class BaseModule extends ModuleSupport implements ITemplateExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		IConfiguration sessionConfig = application.getConfiguration()
				.getConfig("activity");
		if (sessionConfig == null) {
			Logger.error("LiveChat Redis Configuration not set");
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
		Logger.info("LiveChat Redis Server Type: {}", type);
		Logger.info("LiveChat Redis Server Address: {}", servers);
		Logger.info("LiveChat Redis Database: {}", databaseNum);
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
				IActivitySessionService sessionService = new RedisActivitySessionService(
						redisson);
				bind(IActivitySessionService.class).toInstance(sessionService);
			}
		};

	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		this.binderHessianService(binder);

		final Multibinder<ITemplateFragmentProvider> tfProviders = Multibinder
				.newSetBinder(binder, ITemplateFragmentProvider.class);

		for (ITemplateExtension e : filterModules(modules,
				ITemplateExtension.class)) {
			e.registerTemplateProviders(tfProviders);
		}

		final Multibinder<HtmlRenderHook> hooks = Multibinder.newSetBinder(
				binder, HtmlRenderHook.class);

		for (IHtmlHookExtension e : filterModules(modules,
				IHtmlHookExtension.class)) {
			e.registerHtmlHook(hooks);
		}

		final Multibinder<IMoneyIntercepter> moneyIntercepters = Multibinder
				.newSetBinder(binder, IMoneyIntercepter.class);
		moneyIntercepters.addBinding().to(JPYMoneyIntercepter.class);
		// ~ Qualification
		final Multibinder<IActivityQualificationProvider> aqplugin = Multibinder
				.newSetBinder(binder, IActivityQualificationProvider.class);
		for (IQualificationExtension e : filterModules(modules,
				IQualificationExtension.class)) {
			e.registerActivityQualificationProviders(aqplugin);
		}
		// ~ rule
		final Multibinder<IActivityRuleProvider> arplugin = Multibinder
				.newSetBinder(binder, IActivityRuleProvider.class);
		for (IRuleExtension e : filterModules(modules, IRuleExtension.class)) {
			e.registerActivityRuleProviders(arplugin);
		}

		// ~ prize
		final Multibinder<IActivityPrizeProvider> applugin = Multibinder
				.newSetBinder(binder, IActivityPrizeProvider.class);
		for (IPrizeExtension e : filterModules(modules, IPrizeExtension.class)) {
			e.registerActivityPrizeProviders(applugin);
		}

	}

	/**
	 * 缁戝畾Hessian鏈嶅姟绫�
	 * 
	 * @author lijun
	 * @param binder
	 */
	private void binderHessianService(Binder binder) {
		try {
			String baseUrl = Play.application().configuration()
					.getString("sessianRemoteServiceUrl");
			if (baseUrl == null || baseUrl.length() == 0) {
				return;
			}
			Properties p = new Properties();
			ServiceInvokeFactory factory = new HessianInvokeFactory();
			p.load(Play.application().classloader()
					.getResourceAsStream("HessianService.properties"));

			FluentIterable
					.from(p.keySet())
					.filter(k -> k.toString().startsWith("service."))
					.forEach(
							k -> {
								Logger.debug(k.toString());
								Logger.debug("-----------"
										+ p.getProperty(k.toString()));
								Class c = this.loadClass(p.getProperty(k
										.toString()));
								Logger.debug("Class-----------" + c.toString());
								if (c != null) {
									String[] key = k.toString().split("\\.");
									if (key != null && key.length > 1) {
										String url = baseUrl + key[1].trim();
										Logger.debug(url);
										Object servictObject = factory
												.getService(url, c);
										if (servictObject != null) {
											binder.bind(c).toInstance(
													servictObject);
											Logger.debug("binder {} succeed",
													c.getName());
										} else {
											Logger.debug(c.toString()
													+ "servictObject涓虹┖");
										}

									}
								}
							});
		} catch (IOException e) {
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
}

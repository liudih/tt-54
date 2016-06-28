package extensions.mobile;

import handlers.mobile.LoginHandler;
import handlers.mobile.ViewProductHandler;
import handlers.mobile.VisitLogHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mapper.AppVersionMapper;
import mapper.ClientErrorLogMapper;
import mapper.ContactMapper;
import mapper.SettingMapper;
import mapper.VisitLogMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import org.redisson.Config;
import org.redisson.MasterSlaveServersConfig;
import org.redisson.Redisson;
import org.redisson.SentinelServersConfig;

import play.Logger;
import services.mobile.IMobileSessionService;
import services.mobile.MobileSessionService;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.BaseModule;
import extensions.event.IEventExtension;
import extensions.interaction.InteractionModule;
import extensions.member.MemberModule;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;

public class MobileModule extends ModuleSupport implements MyBatisExtension,
		IEventExtension {

	@Override
	public Module getModule(IApplication application) {

		IConfiguration sessionConfig = application.getConfiguration()
				.getConfig("mobile");
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

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("mobile", VisitLogMapper.class);
		service.addMapperClass("mobile", ClientErrorLogMapper.class);
		service.addMapperClass("mobile", SettingMapper.class);
		service.addMapperClass("mobile", ContactMapper.class);
		service.addMapperClass("mobile", AppVersionMapper.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(BaseModule.class, MemberModule.class,
				InteractionModule.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(VisitLogHandler.class));
		eventBus.register(injector.getInstance(LoginHandler.class));
		eventBus.register(injector.getInstance(ViewProductHandler.class));

	}

}

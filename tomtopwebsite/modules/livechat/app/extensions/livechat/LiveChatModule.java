package extensions.livechat;

import handlers.livechat.LiveChatUnactiveSessionMonitorHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.redisson.Config;
import org.redisson.MasterSlaveServersConfig;
import org.redisson.Redisson;
import org.redisson.SentinelServersConfig;

import play.Logger;
import services.livechat.IChatSessionService;
import services.livechat.RedisChatSessionService;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.camel.ICamelExtension;
import extensions.event.IEventExtension;
import extensions.livechat.camel.LiveChatMonitorTimerTrigger;
import extensions.livechat.role.EnquiryRoleProvider;
import extensions.livechat.role.GuestEnquiryRoleProvider;
import extensions.livechat.role.ILiveChatOnDutyCustomerServiceProvider;
import extensions.livechat.role.LiveChatOnDutyCustomerServiceExtension;
import extensions.livechat.role.LiveChatRoleExtension;
import extensions.livechat.role.LiveChatRoleStatusExtension;
import extensions.livechat.role.LiveChatRoleStatusProvider;
import extensions.livechat.role.LtcSessionStatusProvider;
import extensions.livechat.role.SupportRoleProvider;
import extensions.livechat.score.SessionScoreExtension;
import extensions.livechat.score.SessionScoreQuestionProvider;
import extensions.livechat.topic.ChatTopicExtension;
import extensions.livechat.topic.ChatTopicProvider;
import extensions.livechat.topic.WelcomeSentenceExtension;
import extensions.livechat.topic.IWelcomeSentenceProvider;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;

public class LiveChatModule extends ModuleSupport implements
		LiveChatAliasExtension, LiveChatRoleExtension, ICamelExtension,
		LiveChatRoleStatusExtension, IEventExtension {

	@Override
	public Module getModule(IApplication application) {
		IConfiguration sessionConfig = application.getConfiguration()
				.getConfig("livechat");
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
				IChatSessionService sessionService = new RedisChatSessionService(
						redisson);
				bind(IChatSessionService.class).toInstance(sessionService);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {

		final Multibinder<LiveChatAliasResolver> resolvers = Multibinder
				.newSetBinder(binder, LiveChatAliasResolver.class);
		for (LiveChatAliasExtension m : filterModules(modules,
				LiveChatAliasExtension.class)) {
			m.registerAliasResolver(resolvers);
		}

		final Multibinder<ChatTopicProvider> topics = Multibinder.newSetBinder(
				binder, ChatTopicProvider.class);
		for (ChatTopicExtension m : filterModules(modules,
				ChatTopicExtension.class)) {
			m.registerChatTopics(topics);
		}

		final Multibinder<EnquiryRoleProvider> enqProviders = Multibinder
				.newSetBinder(binder, EnquiryRoleProvider.class);
		final Multibinder<SupportRoleProvider> supProviders = Multibinder
				.newSetBinder(binder, SupportRoleProvider.class);
		for (LiveChatRoleExtension m : filterModules(modules,
				LiveChatRoleExtension.class)) {
			m.registerRoles(enqProviders, supProviders);
		}

		final Multibinder<LiveChatRoleStatusProvider> statusProviders = Multibinder
				.newSetBinder(binder, LiveChatRoleStatusProvider.class);
		for (LiveChatRoleStatusExtension m : filterModules(modules,
				LiveChatRoleStatusExtension.class)) {
			m.registerRoleStatus(statusProviders);
		}

		final Multibinder<SessionScoreQuestionProvider> questionProviders = Multibinder
				.newSetBinder(binder, SessionScoreQuestionProvider.class);
		for (SessionScoreExtension m : filterModules(modules,
				SessionScoreExtension.class)) {
			m.registerSessionScores(questionProviders);
		}

		final Multibinder<IWelcomeSentenceProvider> ws = Multibinder
				.newSetBinder(binder, IWelcomeSentenceProvider.class);

		for (WelcomeSentenceExtension m : filterModules(modules,
				WelcomeSentenceExtension.class)) {
			m.registerWelcomeSentence(ws);
		}
		
		final Multibinder<ILiveChatOnDutyCustomerServiceProvider> csproviders = Multibinder
				.newSetBinder(binder,
						ILiveChatOnDutyCustomerServiceProvider.class);
		for (LiveChatOnDutyCustomerServiceExtension m : filterModules(modules,
				LiveChatOnDutyCustomerServiceExtension.class)) {
			m.registerDutyCustomerService(csproviders);
		}
		 
	}

	@Override
	public void registerAliasResolver(Multibinder<LiveChatAliasResolver> binder) {
		binder.addBinding().to(LTCPassthruAliasResolver.class);
	}

	@Override
	public void registerRoles(
			Multibinder<EnquiryRoleProvider> enquiryRoleProviders,
			Multibinder<SupportRoleProvider> supportRoleProviders) {
		enquiryRoleProviders.addBinding().to(GuestEnquiryRoleProvider.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector
				.getInstance(LiveChatUnactiveSessionMonitorHandler.class));
	}

	@Override
	public List<RouteBuilder> getRouteBuilders() {
		return Lists.newArrayList(new LiveChatMonitorTimerTrigger());
	}

	@Override
	public void registerRoleStatus(
			Multibinder<LiveChatRoleStatusProvider> binder) {
		binder.addBinding().to(LtcSessionStatusProvider.class);

	}

}

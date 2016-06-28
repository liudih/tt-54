package session.redis;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

import org.redisson.Config;
import org.redisson.MasterSlaveServersConfig;
import org.redisson.Redisson;
import org.redisson.SentinelServersConfig;

import play.Logger;
import session.ISessionService;
import session.mem.InMemorySessionService;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import extensions.ModuleSupport;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;

public class RedisSessionModule extends ModuleSupport {

	@Override
	public Module getModule(IApplication application) {
		IConfiguration sessionConfig = application.getConfiguration()
				.getConfig("session");
		if (sessionConfig == null) {
			Logger.warn("Redis session not configured, fallback to use In-Memory Session");
			return new AbstractModule() {
				@Override
				protected void configure() {
					bind(ISessionService.class)
							.to(InMemorySessionService.class).in(
									Singleton.class);
				}
			};
		}
		String type = sessionConfig.getString("server_type");
		String password = sessionConfig.getString("server_password");
		Integer databaseNum = sessionConfig.getInt("database", 0);
		List<Object> servers = new LinkedList<Object>();
		try {
			servers.addAll(sessionConfig.getList("server_address"));
		} catch (Exception e) {
			servers.addAll(Arrays.asList(sessionConfig.getString(
					"server_address").split(",")));
		}
		Long timeout = sessionConfig.getLong("timeout", 60 * 60L);
		Logger.info("Session Redis Server Type: {}", type);
		Logger.info("Session Redis Server Address: {}", servers);
		Logger.info("Session Redis Database: {}", databaseNum);
		Logger.info("Session Redis Expiry: {} sec", timeout);
		Logger.info("Session Redis password: {} password", password);
		Config config = new Config();
		if (type == null || "single".equals(type)) {
			config.useSingleServer().setAddress(servers.get(0).toString())
					.setDatabase(databaseNum).setPassword(password);
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
			if(null == password || "".equals(password)){
				
			}else{
				msconfig.setPassword(password);
			}
			msconfig.setDatabase(databaseNum);
		} else if ("sentinel".equals(type)) {
			SentinelServersConfig sconfig = config.useSentinelConnection();
			sconfig.setMasterName("master");
			for (Object a : servers) {
				sconfig.addSentinelAddress(a.toString());
			}
			if(null == password || "".equals(password)){
				
			}else{
				sconfig.setPassword(password);
			}
			sconfig.setDatabase(databaseNum);
			
		} else {
			throw new RuntimeException(
					"Redis config error, session.server_type must be one of ['single', 'master_slave', 'sentinel']");
		}
		Redisson redisson = Redisson.create(config);
		final SessionService sessionService = new SessionService(redisson,
				timeout);

		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(ISessionService.class).toInstance(sessionService);
			}
		};
	}

}

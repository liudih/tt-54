package mybatis;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;

public class MyBatisModule extends ModuleSupport {

	Logger logger = LoggerFactory.getLogger(MyBatisModule.class);

	MyBatisService dbService = null;

	@Override
	public Module getModule(IApplication application) {

		final IConfiguration mybatisConf = application.getConfiguration()
				.getConfig("mybatis");

		if (mybatisConf == null) {
			return null;
		}

		Set<String> dbNames = Sets.newHashSet(Collections2.transform(
				mybatisConf.keys(), new Function<String, String>() {
					@Override
					public String apply(String key) {
						return key.split("\\.")[0];
					}
				}));

		logger.debug("MyBatis DB Names: {}", dbNames);

		Map<String, Properties> dbs = Maps.toMap(dbNames,
				new Function<String, Properties>() {
					@Override
					public Properties apply(String key) {
						logger.debug("Name: {}", key);
						IConfiguration singleConf = mybatisConf.getConfig(key);
						String driver = singleConf.getString("driver");
						String url = singleConf.getString("url");
						String user = singleConf.getString("user");
						String password = singleConf.getString("password");
						String poolSize = singleConf.getString("poolsize");
						if (poolSize == null || poolSize.isEmpty()) {
							poolSize = "10";
						}
						logger.debug("Driver: {}", driver);
						logger.debug("URL: {}", url);

						Properties prop = new Properties();
						prop.setProperty("JDBC.driver", driver);
						prop.setProperty("JDBC.url", url);
						prop.setProperty("JDBC.username", user);
						prop.setProperty("JDBC.password", password);
						prop.setProperty("JDBC.autoCommit", "true");
						prop.setProperty("hikari.connectionTestStatement",
								"SELECT 1");
						prop.setProperty("hikari.maxPoolSize", poolSize);
						return prop;
					}
				});

		dbService = new MyBatisService(dbs);

		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(MyBatisService.class).toInstance(dbService);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		if (dbService == null) {
			return;
		}

		for (IModule m : modules) {
			if (m instanceof MyBatisExtension) {
				MyBatisExtension ext = (MyBatisExtension) m;
				ext.processConfiguration(dbService);
			}
		}

		// mapper classes are registered by now
		dbService.initialize();

		// install all private modules
		for (Module m : dbService.getModules().values()) {
			binder.install(m);
		}
	}

	@Override
	public void onStart(IApplication app, Injector injector) {
	}

	@Override
	public void onStop(IApplication app, Injector injector) {
		if (dbService != null) {
			dbService.shutdown();
		}
	}

}

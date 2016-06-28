package mybatis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Singleton;
import javax.sql.DataSource;

import mybatis.cp.HikariDataSourceProvider;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariDataSource;

import extensions.InjectorInstance;

@Singleton
public class MyBatisService {

	final Logger logger = LoggerFactory.getLogger(MyBatisService.class);

	final Map<String, Properties> configs;
	final Map<String, List<Class<?>>> mappers;
	Map<String, Module> modules;

	public MyBatisService(final Map<String, Properties> dbs) {
		this.configs = dbs;
		this.mappers = new HashMap<String, List<Class<?>>>();
	}

	/**
	 * Register Mapper Class to the database identified by name. This should be
	 * called before initialize() method. Call this method during
	 * <code>processConfiguration</code>.
	 * 
	 * @param dbname
	 * @param mapper
	 * @see MyBatisExtension#processConfiguration(MyBatisService)
	 */
	public void addMapperClass(String dbname, Class<?> mapper) {
		if (!mappers.containsKey(dbname)) {
			mappers.put(dbname, new LinkedList<Class<?>>());
		}
		mappers.get(dbname).add(mapper);
		if (!configs.containsKey(dbname)) {
			logger.error("Registering to a non-existence DB: {}", dbname);
		}
	}

	protected void initialize() {
		this.modules = Maps.transformEntries(configs,
				new EntryTransformer<String, Properties, Module>() {
					@Override
					public Module transformEntry(final String name,
							final Properties config) {
						return new PrivateModule() {
							@Override
							protected void configure() {
								install(new MyBatisModule() {
									@Override
									protected void initialize() {
										environmentId("TOMTOP");
										bindDataSourceProviderType(HikariDataSourceProvider.class);
										bindTransactionFactoryType(JdbcTransactionFactory.class);
										if (mappers.get(name) != null) {
											for (Class<?> clazz : mappers
													.get(name)) {
												addMapperClass(clazz);
											}
										}
									}
								});
								Names.bindProperties(this.binder(), config);
								if (mappers.get(name) != null) {
									for (Class<?> clazz : mappers.get(name)) {
										expose(clazz);
									}
								}
								bind(SqlSessionFactory.class)
										.annotatedWith(Names.named(name))
										.to(SqlSessionFactory.class)
										.in(Scopes.SINGLETON);
								bind(DataSource.class)
										.annotatedWith(Names.named(name))
										.to(DataSource.class)
										.in(Scopes.SINGLETON);
								expose(SqlSessionFactory.class).annotatedWith(
										Names.named(name));
								expose(DataSource.class).annotatedWith(
										Names.named(name));
							}
						};
					}
				});
	}

	protected void shutdown() {
		Set<String> names = configs.keySet();
		for (String name : names) {
			HikariDataSource ds = (HikariDataSource) InjectorInstance
					.getInstance(Key.get(DataSource.class, Names.named(name)));
			if (ds != null) {
				ds.close();
			}
		}
	}

	public Map<String, Module> getModules() {
		return modules;
	}

	public Set<String> getNames() {
		return configs.keySet();
	}
}

package com.tomtop.website.mybatis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.bonecp.BoneCPProvider;

import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.tomtop.website.migration.ICommand;

public class MyBatisService {

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
	}

	public void initialize() {
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
										bindDataSourceProviderType(BoneCPProvider.class);
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

	public Map<String, Module> getModules() {
		return modules;
	}

	public Set<String> getNames() {
		return configs.keySet();
	}

	public void configure(ICommand cmd) {
		if (cmd instanceof IMyBatisConfig) {
			((IMyBatisConfig) cmd).config(this);
		}
	}

	public static Properties getProperties(String driver, String url,
			String user, String password) {
		Properties prop = new Properties();
		prop.setProperty("JDBC.driver", driver);
		prop.setProperty("JDBC.url", url);
		prop.setProperty("JDBC.username", user);
		prop.setProperty("JDBC.password", password);
		prop.setProperty("JDBC.autoCommit", "true");
		return prop;
	}

}

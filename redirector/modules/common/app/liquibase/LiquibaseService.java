package liquibase;

import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import mybatis.MyBatisService;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

@Singleton
public class LiquibaseService {

	@Inject
	Injector injector;

	@Inject
	MyBatisService mybatis;

	Map<String, Liquibase> liquibases;

	Map<String, DataSource> datasources;

	public Liquibase getLiquibaseInstance(String name) {
		if (liquibases == null) {
			initialize();
		}
		return liquibases.get(name);
	}

	public DataSource getDataSource(String name) {
		if (datasources == null) {
			initialize();
		}
		return datasources.get(name);
	}

	protected synchronized void initialize() {
		Set<String> names = mybatis.getNames();
		datasources = Maps.toMap(names, new Function<String, DataSource>() {
			@Override
			public DataSource apply(String name) {
				DataSource ds = injector.getInstance(Key.get(DataSource.class,
						Names.named(name)));
				return ds;
			}
		});
		liquibases = Maps.toMap(names, new Function<String, Liquibase>() {
			@Override
			public Liquibase apply(String name) {
				try {
					DataSource ds = datasources.get(name);
					Connection conn = ds.getConnection();
					Liquibase lb = new Liquibase("liquibase/" + name + ".xml",
							new ClassLoaderResourceAccessor(),
							new JdbcConnection(conn));
					return lb;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

}

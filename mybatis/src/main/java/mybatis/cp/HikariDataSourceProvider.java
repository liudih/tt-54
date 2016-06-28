package mybatis.cp;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;

public class HikariDataSourceProvider implements Provider<DataSource> {

	private final HikariDataSource dataSource = new HikariDataSource();

	@Override
	public DataSource get() {
		return dataSource;
	}

	@Inject
	public void setDriverClass(@Named("JDBC.driver") final String driverClass) {
		dataSource.setDriverClassName(driverClass);
	}

	@Inject
	public void setJdbcUrl(@Named("JDBC.url") String jdbcUrl) {
		dataSource.setJdbcUrl(jdbcUrl);
	}

	@Inject
	public void setPassword(@Named("JDBC.password") String password) {
		dataSource.setPassword(password);
	}

	@Inject
	public void setUsername(@Named("JDBC.username") String username) {
		dataSource.setUsername(username);
	}

	@com.google.inject.Inject(optional = true)
	public void setConnectionTestStatement(
			@Named("hikari.connectionTestStatement") String connectionTestStatement) {
		dataSource.setConnectionTestQuery(connectionTestStatement);
	}

	@com.google.inject.Inject(optional = true)
	public void setMaxConnectionsPerPartition(
			@Named("hikari.maxPoolSize") int maxPoolSize) {
		dataSource.setMaximumPoolSize(maxPoolSize);
	}

}

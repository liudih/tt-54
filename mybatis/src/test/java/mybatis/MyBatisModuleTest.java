package mybatis;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.ibatis.annotations.Select;

import com.google.common.collect.Sets;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;
import extensions.runtime.impl.PropertiesConfiguration;
import framework.Framework;

public class MyBatisModuleTest extends TestCase {

	public void testModule() throws Exception {
		Framework f = Framework.create(new IApplication() {
			@Override
			public IConfiguration getConfiguration() {
				try {
					Properties p = new Properties();
					p.load(getClass().getResourceAsStream("/test.properties"));
					IConfiguration config = new PropertiesConfiguration(p);
					return config;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public boolean isDev() {
				return false;
			}

			@Override
			public boolean isProd() {
				return false;
			}

			@Override
			public boolean isTest() {
				return true;
			}

		}, Arrays.asList(MyBatisModule.class, SimpleMyBatisModule.class));
		f.start();

		DataSource ds = f.getInjector().getInstance(
				Key.get(DataSource.class, Names.named("test")));
		Connection conn = ds.getConnection();
		assertNotNull(conn);

		SimpleMapper mapper = f.getInjector().getInstance(SimpleMapper.class);
		Date d = mapper.getServerDate();
		System.out.println("Date: " + d);
		assertNotNull(d);
	}

	public static class SimpleMyBatisModule extends ModuleSupport implements
			MyBatisExtension {

		@Override
		public Module getModule(IApplication application) {
			return null;
		}

		@Override
		public Set<Class<? extends IModule>> getDependentModules() {
			return Sets.newHashSet(MyBatisModule.class);
		}

		@Override
		public void processConfiguration(MyBatisService service) {
			service.addMapperClass("test", SimpleMapper.class);
		}

	}

	public static interface SimpleMapper {

		@Select("SELECT now()")
		Date getServerDate();

	}
}

package common.test;

import static common.test.Helpers.myBatisInMemoryDb;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.Map;

import liquibase.LiquibaseService;
import play.Logger;

import com.google.common.collect.Lists;

import extensions.IModule;
import extensions.InjectorInstance;
import extensions.ModularGlobal;

public abstract class ModuleTest {

	public abstract String[] mybatisNames();

	public abstract Class<? extends IModule>[] moduleClasses();

	protected Map<String, String> additionalConfig(Map<String, String> config) {
		return config;
	}

	public void run(Runnable runnable) {
		final Object testClass = this;
		running(fakeApplication(
				additionalConfig(myBatisInMemoryDb(mybatisNames())),
				new ModularGlobal(Lists.newArrayList(moduleClasses()))),
				() -> {
					try {
						LiquibaseService dbUpdate = InjectorInstance
								.getInjector().getInstance(
										LiquibaseService.class);
						for (String name : mybatisNames()) {
							try {
								dbUpdate.getLiquibaseInstance(name).update(
										"test");
							} catch (Exception e) {
								Logger.error("DB " + name + " Error", e);
							}
						}
						InjectorInstance.getInjector().injectMembers(testClass);
						runnable.run();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}
}

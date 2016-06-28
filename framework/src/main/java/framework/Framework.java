package framework;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.IModule;
import extensions.InjectorInstance;
import extensions.runtime.IApplication;

/**
 * Base Global initialization procedure.
 * 
 * @author kmtong
 *
 */
public class Framework {

	final Logger logger = LoggerFactory.getLogger(Framework.class);

	protected Injector injector;
	final IApplication application;

	final List<? extends IModule> modules;

	public Framework(IApplication application, IModule... modules) {
		this.modules = Arrays.asList(modules);
		this.application = application;
	}

	public Framework(IApplication application, List<Class<? extends IModule>> moduleClazz) {
		Set<Class<? extends IModule>> instantiated = Sets.newLinkedHashSet();
		List<IModule> ms = resolveModules(moduleClazz, instantiated);
		this.modules = ms;
		this.application = application;
		logger.debug("Modules: {}", instantiated);
	}

	protected static List<IModule> resolveModules(
			Collection<Class<? extends IModule>> modules,
			Set<Class<? extends IModule>> instantiated) {
		List<IModule> ms = new LinkedList<IModule>();
		for (Class<? extends IModule> clazz : modules) {
			try {
				if (!instantiated.contains(clazz)) {
					IModule m = clazz.newInstance();
					instantiated.add(clazz);
					if (m.getDependentModules() != null) {
						ms.addAll(resolveModules(m.getDependentModules(),
								instantiated));
					}
					ms.add(m);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return ms;
	}

	public static Framework create(IApplication application,
			List<Class<? extends IModule>> modules) {
		Framework framework = new Framework(application, modules);
		return framework;
	}

	public void start() {
		List<Module> guiceModules = new LinkedList<Module>();
		for (IModule m : modules) {
			Module gm = m.getModule(application);
			if (gm != null) {
				guiceModules.add(gm);
			}
		}

		guiceModules.add(new AbstractModule() {
			@Override
			protected void configure() {
				for (IModule m : modules) {
					m.configBinderExtras(modules, binder());
				}
			}
		});

		// initializing injector
		this.injector = Guice.createInjector(guiceModules);
		InjectorInstance.setInjector(injector);

		for (IModule m : modules) {
			m.onStart(application, injector);
		}
	}

	public void stop() {
		for (IModule m : Lists.reverse(modules)) {
			m.onStop(application, injector);
		}
		InjectorInstance.setInjector(null);
		injector = null;
	}

	public Injector getInjector() {
		return injector;
	}

}

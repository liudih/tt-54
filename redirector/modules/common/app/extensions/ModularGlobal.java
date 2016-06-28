package extensions;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import play.Application;
import play.GlobalSettings;
import play.Logger;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Base Global initialization procedure.
 * 
 * @author kmtong
 *
 */
public class ModularGlobal extends GlobalSettings {

	protected Injector injector;
	protected Application application;

	final List<? extends IModule> modules;

	public ModularGlobal(IModule... modules) {
		this.modules = Arrays.asList(modules);
	}

	public ModularGlobal(List<Class<? extends IModule>> moduleClazz) {
		Set<Class<? extends IModule>> instantiated = Sets.newLinkedHashSet();
		List<IModule> ms = resolveModules(moduleClazz, instantiated);
		this.modules = ms;
		Logger.debug("Modules: {}", instantiated);
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

	@Override
	public void onStart(Application app) {
		this.application = app;
		List<Module> guiceModules = new LinkedList<Module>();
		for (IModule m : modules) {
			Module gm = m.getModule(app);
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
			m.onStart(app, injector);
		}
	}

	@Override
	public void onStop(Application app) {
		for (IModule m : modules) {
			m.onStop(app, injector);
		}
		InjectorInstance.setInjector(null);
		injector = null;
	}

	@Override
	public <A> A getControllerInstance(Class<A> clazz) throws Exception {
		return injector.getInstance(clazz);
	}

	public Injector getInjector() {
		return injector;
	}

}

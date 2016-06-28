package extensions;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Collections2;
import com.google.inject.Binder;
import com.google.inject.Injector;

import extensions.runtime.IApplication;

public abstract class ModuleSupport implements IModule {

	public Set<Class<? extends IModule>> getDependentModules() {
		return null;
	}

	/**
	 * Allow modules to setup its own multibinder
	 * 
	 * @param modules
	 * @param binder
	 */
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
	}

	/**
	 * Called on application start, after module initialization.
	 */
	public void onStart(IApplication app, Injector injector) {
	}

	/**
	 * Called on application stop
	 */
	public void onStop(IApplication app, Injector injector) {
	}

	/**
	 * Helper to get implementation classes filtered.
	 * 
	 * @param modules
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> Collection<T> filterModules(
			Collection<? extends IModule> modules, Class<T> clazz) {
		final Collection<T> exts = Collections2.transform(
				Collections2.filter(modules, m -> clazz.isInstance(m)),
				m -> (T) m);
		return exts;
	}
}

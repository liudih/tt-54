package extensions;

import java.util.List;
import java.util.Set;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.runtime.IApplication;

public interface IModule {

	/**
	 * define the set of dependent modules
	 * 
	 * @return
	 */
	Set<Class<? extends IModule>> getDependentModules();

	/**
	 * Define the Guice Module used for configuring this Application Module.
	 */
	Module getModule(IApplication application);

	/**
	 * Allow modules to setup its own multibinder
	 * 
	 * @param modules
	 * @param binder
	 */
	void configBinderExtras(List<? extends IModule> modules, Binder binder);

	/**
	 * Called on application start, after module initialization.
	 */
	void onStart(IApplication app, Injector injector);

	/**
	 * Called on application stop
	 */
	void onStop(IApplication app, Injector injector);

}

package extensions.hessian;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.runtime.IApplication;

public class HessianServiceModule extends ModuleSupport {

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		HessianRegistrar reg = new HessianRegistrar();
		for (HessianServiceExtension e : filterModules(modules,
				HessianServiceExtension.class)) {
			e.registerRemoteService(reg);
		}

		// ---- organize services into injectable objects
		Multibinder<HessianServiceDefinition> services = Multibinder
				.newSetBinder(binder, HessianServiceDefinition.class);
		for (HessianServiceDefinition sd : reg.getServiceDefinitions()) {
			services.addBinding().toInstance(sd);
		}
	}

	@Override
	public void onStart(IApplication app, Injector injector) {
		// XXX publish all services to a remote registry (redis)
		// for service lookup
	}

	@Override
	public void onStop(IApplication app, Injector injector) {
		// XXX remove from remote registry (redis)
	}

}

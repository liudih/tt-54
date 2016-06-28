package extensions.camel;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import play.Logger;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.runtime.IApplication;

public class CamelModule extends ModuleSupport {

	CamelContext context;

	@Override
	public Module getModule(IApplication application) {
		this.context = new DefaultCamelContext();
		return null;
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		for (ICamelExtension e : filterModules(modules, ICamelExtension.class)) {
			List<RouteBuilder> rb = e.getRouteBuilders();
			if (rb != null) {
				for (RouteBuilder r : rb) {
					try {
						context.addRoutes(r);
					} catch (Exception e1) {
						Logger.error("Add Route Failure", e1);
					}
				}
			}
		}
		binder.bind(CamelContext.class).toInstance(context);
	}

	@Override
	public void onStart(IApplication app, Injector injector) {
		try {
			context.start();
			Logger.info("Camel Started");
		} catch (Exception e) {
			Logger.error("Camel Context Start Error", e);
		}
	}

	@Override
	public void onStop(IApplication app, Injector injector) {
		try {
			context.stop();
			Logger.info("Camel Stopped");
		} catch (Exception e) {
			Logger.error("Camel Context Stop Error", e);
		}
	}

}

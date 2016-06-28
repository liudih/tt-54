package extensions;

import java.lang.reflect.Method;
import java.util.List;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Request;

import com.google.inject.Injector;

import extensions.filter.internal.InterceptedAction;
import extensions.runtime.IApplication;
import extensions.runtime.PlayApplication;
import framework.Framework;

/**
 * Base Global initialization procedure.
 * 
 * @author kmtong
 *
 */
public class ModularGlobal extends GlobalSettings {

	final List<Class<? extends IModule>> modules;
	IApplication wrappedApplication;
	Framework framework;

	public ModularGlobal(List<Class<? extends IModule>> moduleClazz) {
		this.modules = moduleClazz;
		Logger.debug("Modules: {}", moduleClazz);
	}

	@Override
	public void onStart(Application app) {
		this.wrappedApplication = new PlayApplication(app);
		framework = Framework.create(wrappedApplication, modules);
		framework.start();
	}

	@Override
	public void onStop(Application app) {
		if (framework != null) {
			framework.stop();
		}
	}

	@Override
	public <A> A getControllerInstance(Class<A> clazz) throws Exception {
		return framework.getInjector().getInstance(clazz);
	}

	@Override
	public Action<?> onRequest(Request request, Method method) {
		if (framework.getInjector() != null) {
			final Action<?> delegateAction = super.onRequest(request, method);
			return new InterceptedAction(delegateAction,
					framework.getInjector());
		} else {
			Logger.info("Application Stopped, Request not fullfilled");
			return null;
		}
	}

	public Injector getInjector() {
		return framework.getInjector();
	}

}

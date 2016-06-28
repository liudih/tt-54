import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import play.Logger;
import play.Play;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

import com.google.common.collect.FluentIterable;

import controllers.base.Home;
import extensions.IModule;
import extensions.ModularGlobal;

public class Global extends ModularGlobal {

	public Global() throws Exception {
		super(getModules());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends EssentialFilter> Class<T>[] filters() {
		return new Class[] { GzipFilter.class };
	}

	@Override
	public Promise<Result> onHandlerNotFound(RequestHeader request) {
		return Home.notFoundPromiseResult();
	}

	@Override
	public Promise<Result> onError(RequestHeader request, Throwable t) {
		if (Play.isDev()) {
			return null;
		}
		return Home.errorPromiseResult(request, t);
	}

	@SuppressWarnings("unchecked")
	protected static List<Class<? extends IModule>> getModules()
			throws Exception {
		Properties p = new Properties();
		String moduleconf = System.getProperty("module.config");
		if (moduleconf != null) {
			Logger.info("Loading from Module Configuration File: {}",
					moduleconf);
			p.load(new FileInputStream(moduleconf));
		} else {
			// default module config
			p.load(Global.class.getResourceAsStream("modules.properties"));
		}
		List<?> clazzes = FluentIterable.from(p.keySet())
				.filter(k -> k.toString().startsWith("module."))
				.transform(k -> p.getProperty(k.toString()))
				.transform(clazz -> loadClass(clazz)).filter(c -> c != null)
				.toList();
		return (List<Class<? extends IModule>>) clazzes;
	}

	@SuppressWarnings("unchecked")
	protected static Class<? extends IModule> loadClass(String clazz) {
		try {
			return (Class<? extends IModule>) Class.forName(clazz);
		} catch (Exception e) {
			Logger.error("Module class error: " + clazz, e);
			return null;
		}
	}
}

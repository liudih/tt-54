package interceptors;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

import extensions.ModuleSupport;
import extensions.runtime.IApplication;

/**
 * Refer: https://github.com/google/guice/wiki/AOP
 * 
 * @author kmtong
 *
 */
public class InterceptorsModule extends ModuleSupport {

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {

				AbstractMethodCaching cacher = new GuavaCaching();
				requestInjection(cacher);

				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(CacheResult.class), cacher);

				ConductRecordInterceptor record = new ConductRecordInterceptor();
				requestInjection(record);
				bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(ConductRecord.class), record);
			}
		};
	}

}

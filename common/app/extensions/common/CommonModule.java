package extensions.common;

import interceptors.InterceptorsModule;

import java.util.Set;

import mybatis.MyBatisModule;
import session.redis.RedisSessionModule;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import events.core.SystemStartedEvent;
import events.core.SystemStoppedEvent;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.camel.CamelModule;
import extensions.event.EventBusModule;
import extensions.filter.FilterModule;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.hessian.HessianServiceModule;
import extensions.runtime.IApplication;
import filters.common.CookieTrackingFilter;

public class CommonModule extends ModuleSupport implements IFilterExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(FilterModule.class, EventBusModule.class,
				InterceptorsModule.class, MyBatisModule.class,
				RedisSessionModule.class, CamelModule.class,
				HessianServiceModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(CookieTrackingFilter.class);
	}

	@Override
	public void onStart(IApplication app, Injector injector) {
		super.onStart(app, injector);
		if (injector != null) {
			EventBus bus = injector.getInstance(EventBus.class);
			if (bus != null) {
				bus.post(new SystemStartedEvent());
			}
		}
	}

	@Override
	public void onStop(IApplication app, Injector injector) {
		if (injector != null) {
			EventBus bus = injector.getInstance(EventBus.class);
			if (bus != null) {
				bus.post(new SystemStoppedEvent());
			}
		}
		super.onStop(app, injector);
	}

}

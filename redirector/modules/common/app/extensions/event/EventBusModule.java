package extensions.event;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import play.Application;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;

/**
 * Refer to the official Guava documentation on EventBus:
 * https://code.google.com/p/guava-libraries/wiki/EventBusExplained
 * 
 * @author kmtong
 */
public class EventBusModule extends ModuleSupport {

	private EventBus eventBus;
	private Collection<IEventExtension> extensions;
	private ForkJoinPool mainPool = new ForkJoinPool();

	@Override
	public Module getModule(Application application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				eventBus = new AsyncEventBus(mainPool);
				bind(EventBus.class).toInstance(eventBus);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		this.extensions = filterModules(modules, IEventExtension.class);
	}

	@Override
	public void onStart(Application app, Injector injector) {
		for (IEventExtension e : extensions) {
			e.registerListener(eventBus, injector);
		}
		eventBus.register(new DeadEventHandler());
	}

}

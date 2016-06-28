package extensions.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;

import extensions.IExtensionPoint;

/**
 * Used to register internal events handler (within JVM)
 * 
 * See: https://code.google.com/p/guava-libraries/wiki/EventBusExplained
 * 
 * @author kmtong
 * @see Subscribe
 */
public interface IEventExtension extends IExtensionPoint {

	/**
	 * Subscribe to the event by defining @Subscribe in the method with a sole
	 * parameter of the Event Type.
	 * 
	 * You can use the given Injector to instantiate your Listener object.
	 * 
	 * @param eventBus
	 * @param injector
	 */
	void registerListener(EventBus eventBus, Injector injector);

}

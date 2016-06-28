package extensions;

import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Helper to get the internal injector instance after application started.
 * 
 * @author kmtong
 *
 */
public class InjectorInstance {

	public static Injector injector;

	public static Injector getInjector() {
		return injector;
	}

	public static void setInjector(Injector injector) {
		InjectorInstance.injector = injector;
	}

	public static <T> T getInstance(Class<T> clazz) {
		return getInjector().getInstance(clazz);
	}

	public static <T> T getInstance(Key<T> key) {
		if (getInjector() != null) {
			return getInjector().getInstance(key);
		}
		return null;
	}
}

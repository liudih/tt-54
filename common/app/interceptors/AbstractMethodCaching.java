package interceptors;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import play.Configuration;
import play.Logger;
import play.Play;

/**
 * Rough implementation to cache method result
 * 
 * @author kmtong
 *
 */
public abstract class AbstractMethodCaching implements MethodInterceptor {

	final protected Configuration config;

	public abstract Object get(String key, String cacheName);

	public abstract void set(String key, Object obj, int expInSec,
			String cacheName);

	public AbstractMethodCaching() {
		config = Play.application().configuration().getConfig("cache");
	}

	public Configuration getConfig() {
		return config;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String key = genKey(invocation);
		CacheResult settings = invocation.getMethod().getAnnotation(
				CacheResult.class);
		if (settings == null) {
			// accidentally called in?
			return invocation.proceed();
		}
		if (settings.debug()) {
			Logger.debug("CacheKey: {}", key);
		}
		String cacheName = settings.value().isEmpty() ? null : settings.value();
		Object result = get(key, cacheName);
		if (result == null) {
			result = invocation.proceed();
			int expiration = settings.expiration();
			if (config != null && settings.value() != null
					&& !settings.value().isEmpty()) {
				Integer exp = config.getInt(cacheName);
				if (exp != null) {
					expiration = exp;
					if (settings.debug()) {
						Logger.debug("Expiration {}: {}", settings.value(),
								expiration);
					}
				}
			}
			if (expiration > 0 && result != null) {
				set(key, result, expiration, cacheName);
			}
		} else {
			if (settings.debug()) {
				Logger.debug("Cache Hit! {}", key);
			}
		}
		return result;
	}

	private String genKey(MethodInvocation invocation) {
		return invocation.getMethod().getDeclaringClass().getName() + ":"
				+ invocation.getMethod().getName() + "("
				+ Arrays.toString(invocation.getArguments()) + ")";
	}

}

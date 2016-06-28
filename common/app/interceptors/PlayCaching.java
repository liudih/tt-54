package interceptors;

import javax.inject.Singleton;

import play.cache.Cache;

/**
 * Rough implementation to cache method result using Play internal cache
 * mechanism.
 * 
 * @author kmtong
 *
 */
@Singleton
public class PlayCaching extends AbstractMethodCaching {

	public PlayCaching() {
		super();
	}

	@Override
	public Object get(String key, String cacheName) {
		return Cache.get(key);
	}

	@Override
	public void set(String key, Object obj, int expInSec, String cacheName) {
		Cache.set(key, obj, expInSec);
	}

}

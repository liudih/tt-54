package interceptors;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import play.Configuration;
import play.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.typesafe.config.ConfigValue;

public class GuavaCaching extends AbstractMethodCaching {

	final Cache<Object, Object> cache;
	final Map<String, Cache<Object, Object>> cacheMap;

	public GuavaCaching() {
		super();
		this.cacheMap = Maps.newHashMap();
		Configuration config = getConfig();
		if (config != null) {
			Set<Entry<String, ConfigValue>> specificConfig = config.entrySet();
			for (Entry<String, ConfigValue> c : specificConfig) {
				int expiry = Integer.parseInt(c.getValue().render());
				String cache = c.getKey();
				Logger.debug("Cache {} expiry {}", cache, expiry);
				Cache<Object, Object> specCache = CacheBuilder.newBuilder()
						.expireAfterWrite(expiry, TimeUnit.SECONDS)
						.maximumSize(1000).build();
				cacheMap.put(cache, specCache);
			}
		}
		cache = CacheBuilder.newBuilder().maximumSize(1000).build();
	}

	@Override
	public Object get(String key, String cacheName) {
		if (cacheMap.containsKey(cacheName)) {
			return cacheMap.get(cacheName).getIfPresent(key);
		} else {
			return cache.getIfPresent(key);
		}
	}

	@Override
	public void set(String key, Object obj, int expInSec, String cacheName) {
		if (cacheMap.containsKey(cacheName)) {
			cacheMap.get(cacheName).put(key, obj);
		} else {
			cache.put(key, obj);
		}
	}

}

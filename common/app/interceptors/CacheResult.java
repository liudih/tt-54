package interceptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheResult {

	/**
	 * Cache timeout configuration key, prefixed with "cache" i.e. if you wanna
	 * use config key's setting "cache.price", set this value to "price".
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * Cache Expiration time in Seconds, default to 300 seconds (5 minutes)
	 * 
	 * @return
	 */
	int expiration() default 300;

	boolean debug() default false;
}

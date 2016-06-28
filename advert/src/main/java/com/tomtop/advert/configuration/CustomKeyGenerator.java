package com.tomtop.advert.configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Component;

/**
 * 自定义的缓存key生成
 * 
 * @author liulj
 *
 */
@Component("customKeyGenerator")
public class CustomKeyGenerator implements KeyGenerator {

	public static Object generateKey(Object target, Method method,
			Object... params) {
		Object[] destObjects = Arrays.copyOf(params, params.length + 2);
		if (params.length > 0) {
			destObjects[params.length] = target.getClass().getName();
			destObjects[params.length + 1] = method.getName();
		} else {
			destObjects[0] = target.getClass().getName();
			destObjects[1] = method.getName();
		}
		return new SimpleKey(destObjects);
	}

	@Override
	public Object generate(Object target, Method method, Object... params) {
		// TODO Auto-generated method stub
		return CustomKeyGenerator.generateKey(target, method, params);
	}
}

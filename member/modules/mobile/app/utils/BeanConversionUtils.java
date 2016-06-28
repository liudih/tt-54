package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanConversionUtils {

	public static Map<String, Object> beanToMap(Object obj) {
		if (null == obj) {
			throw new NullPointerException("obj is null");
		}
		Map<String, Object> map = new HashMap<>();

		Class<?> clazz = obj.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				String methodName = getGetMethodName(fieldName);
				try {
					Object value = invokingMethod(obj, methodName);
					if (value != null) {
						map.put(fieldName, value);
					}
				} catch (Exception e) {
					return null;
				}
			}
		}
		return map;
	}

	/**
	 * 根据字段名称，返回该字段的get方法名
	 * 
	 * @param str
	 * @return
	 */
	public static String getGetMethodName(String str) {
		if (str == null || "".equals(str))
			return null;
		String first = str.substring(0, 1);
		first = first.toUpperCase();
		return "get" + first + str.substring(1);
	}

	/**
	 * 根据字段名称，返回该字段的set方法名
	 * 
	 * @param str
	 * @return
	 */
	public static String getSetMethodName(String str) {
		if (str == null || "".equals(str))
			return null;
		String first = str.substring(0, 1);
		first = first.toUpperCase();
		return "set" + first + str.substring(1);
	}

	/**
	 * 调用方法
	 * 
	 * @param dto
	 *            调用底层方法的对象
	 * @param methodName
	 *            方法名称
	 * @param arguments
	 *            用于方法调用的参数，没有参数可以为null
	 * @return
	 * @throws Exception
	 */
	public static Object invokingMethod(Object dto, String methodName,
			Object... arguments) throws Exception {
		Class<?>[] objClass = null;
		if (arguments != null) {
			objClass = new Class[arguments.length];
			// 获取参数的类型
			for (int i = 0; i < arguments.length; i++)
				objClass[i] = arguments[i].getClass();
		}
		Method method = dto.getClass().getMethod(methodName, objClass);
		return method.invoke(dto, arguments);
	}

}

package com.tomtop.product.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * appliction 工具类，(获取application上下文)
 * 
 * @author liulj
 *
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

	/**
	 * appliction上下文
	 */
	private static ApplicationContext applicationContext;

	@Override
	public final void setApplicationContext(
			ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		ApplicationContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取application上下文
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 通过class获取bean
	 * 
	 * @param clas
	 * @return
	 */
	public static <T> T getBean(Class<T> clas) {
		return applicationContext.getBean(clas);
	}

	/**
	 * 通过bean名称获取bean
	 * 
	 * @param clas
	 * @return
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
}

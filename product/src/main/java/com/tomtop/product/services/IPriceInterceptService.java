package com.tomtop.product.services;

import com.tomtop.product.price.PriceInterceptContext;

/**
 * 价格拦截器
 * 
 * @author liulj
 *
 */
public interface IPriceInterceptService {
	/**
	 * 拦截价格处理
	 * 
	 * @param monery
	 * @param currency
	 * @return
	 */
	public String intercept(PriceInterceptContext context, double money,
			String currency);
}

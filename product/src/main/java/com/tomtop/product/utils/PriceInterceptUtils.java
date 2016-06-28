package com.tomtop.product.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomtop.product.price.PriceInterceptContext;
import com.tomtop.product.services.IPriceInterceptService;

/**
 * 价格拦截器工具类
 * 
 * @author liulj
 *
 */
@Component("priceInterceptUtils")
public class PriceInterceptUtils {

	@Autowired
	List<IPriceInterceptService> intercepts;

	/**
	 * 获取价格，执行拦截，执行后的价格
	 * 
	 * @param money
	 * @param currency
	 * @return 返回 执行拦截，执行后的价格
	 */
	public String money(double money, String currency) {
		if (intercepts != null && intercepts.size() > 0) {
			return intercepts.get(0).intercept(
					new PriceInterceptContext(intercepts), money, currency);
		} else {
			return String.valueOf(money);
		}
	}
}

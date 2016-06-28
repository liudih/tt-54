package com.tomtop.product.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.tomtop.product.price.PriceInterceptContext;
import com.tomtop.product.services.IPriceInterceptService;

/**
 * 价格拦截器
 * 
 * @author liulj
 *
 */
@Service
public class PriceJpyInterceptServiceImpl implements IPriceInterceptService {
	@Override
	public String intercept(PriceInterceptContext context, double monery,
			String currency) {
		if (currency.equals("JPY")) {
			return new BigDecimal(monery).setScale(0, RoundingMode.HALF_UP)
					.toString();
		} else {
			return context.callNextIntercept(context, monery, currency);
		}
	}
}

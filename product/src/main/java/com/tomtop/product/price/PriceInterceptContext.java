package com.tomtop.product.price;

import java.util.List;

import com.tomtop.product.services.IPriceInterceptService;

/**
 * 价格拦截器的上下文
 * 
 * @author liulj
 *
 */
public class PriceInterceptContext {

	private int currntIndex = 0;

	private List<IPriceInterceptService> intercepts;

	public PriceInterceptContext(List<IPriceInterceptService> intercepts) {
		this.intercepts = intercepts;
	}

	public String callNextIntercept(PriceInterceptContext context,
			double monery, String currency) {
		context.setCurrntIndex(context.getCurrntIndex() + 1);
		if (context.getCurrntIndex() >= context.getIntercepts().size()) {
			return String.valueOf(monery);
		} else {
			return context.getIntercepts().get(context.getCurrntIndex())
					.intercept(context, monery, currency);
		}
	}

	public List<IPriceInterceptService> getIntercepts() {
		return intercepts;
	}

	public int getCurrntIndex() {
		return currntIndex;
	}

	public void setCurrntIndex(int currntIndex) {
		this.currntIndex = currntIndex;
	}
}

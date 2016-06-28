package intercepters;

import java.text.DecimalFormat;
import java.util.Set;

import play.Logger;
import play.mvc.Http.Context;
import services.IMoneyService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import context.ContextUtils;
import context.WebContext;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class MoneyIntercepterHelper {
	@Inject(optional = true)
	Set<IMoneyIntercepter> intercepter;

	@Inject(optional = true)
	IMoneyService service;

	/**
	 * 拦截策略是：当某一个intercepter返回值不为null,则立马返回,后面的intercepter不会再执行
	 * 
	 * @param money
	 * @return
	 */
	public String intercept(Double money) {
		WebContext webCtx = null;
		return this.intercept(money, webCtx);
	}
	
	public String intercept(Double money,String currencyCode) {
		Logger.debug("MoneyIntercepter currency:{}",currencyCode);
		//Context httpCtx = Context.current();
		WebContext webCtx = new WebContext();
		webCtx.setCurrencyCode(currencyCode);
		return this.intercept(money, webCtx);
	}

	private String intercept(Double money, WebContext webCtx) {
		if(service != null){
			//Logger.debug("调用服务端方法处理money");
			return service.money(money, webCtx);
		}
		if (intercepter == null) {
			Logger.debug("未发现IMoneyIntercepter注入类,所以IMoneyIntercepter处理被忽略");
			return null;
		}
		for (IMoneyIntercepter mi : intercepter) {
			String result = mi.intercept(money, webCtx);
			if (result != null && result.length() > 0) {
				return result;
			}
		}
		return null;
	}
}

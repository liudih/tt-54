package services.base;

import intercepters.IMoneyIntercepter;

import java.util.Set;

import services.IMoneyService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import context.WebContext;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class MoneyService implements IMoneyService {

	@Inject(optional = true)
	Set<IMoneyIntercepter> intercepter;
	
	@Override
	public String money(Double money,WebContext webCtx) {
		if (intercepter == null) {
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

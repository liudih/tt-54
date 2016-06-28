package intercepters;

import java.text.DecimalFormat;

import play.Logger;
import play.mvc.Http.Context;
import session.ISessionService;
import valueobjects.base.LoginContext;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import context.ContextUtils;
import context.WebContext;

/**
 * 日元最小是1元 如果有小数点 paypal会支付不成功 需特殊处理
 * 
 * @author lijun
 *
 */
@Singleton
public class JPYMoneyIntercepter implements IMoneyIntercepter {

	@Inject(optional = true)
	ISessionService sessionService;

	@Override
	public String intercept(Double money, WebContext webCtx) {
		if (money == null) {
			return null;
		}
		String currency = null;
		if (webCtx != null && webCtx.getCurrencyCode() != null) {
			currency = webCtx.getCurrencyCode();
		} else {
			if (sessionService == null) {
				Logger.warn("can not find ISessionService,JPYMoneyIntercepter be ignored");
				return null;
			}
			Context httpCtx = Context.current();
			WebContext ctx = ContextUtils.getWebContext(httpCtx);

			LoginContext loginCtx = (LoginContext) sessionService.get(
					"LOGIN_CONTEXT", ctx);
			if (loginCtx == null || loginCtx.getCurrencyCode() == null) {
				//Logger.debug("获取不到LoginContext,所以日元拦截器被忽略");
				return null;
			}
			// 日元最小是1元 如果有小数点 paypal会支付不成功 需特殊处理
			currency = loginCtx.getCurrencyCode();
		}

		if (currency != null && "JPY".equals(currency)) {
			// 四舍五入
			long moneyFloat = Math.round(money);
			DecimalFormat df = new DecimalFormat("#,##0");
			return df.format(moneyFloat);
		}
		return null;
	}
}

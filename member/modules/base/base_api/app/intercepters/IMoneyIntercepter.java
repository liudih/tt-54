package intercepters;

import context.WebContext;

/**
 * money拦截器
 * 
 * @author lijun
 *
 */
public interface IMoneyIntercepter {

	/**
	 * 重新对money进行处理
	 * 
	 * @param money
	 * @return 
	 */
	public String intercept(Double money,WebContext webCtx);
}

package services;

import context.WebContext;

/**
 * money 服务类,主要提供格式化数据作用,比如保留2位小数位
 * 
 * @author lijun
 *
 */
public interface IMoneyService {
	/**
	 * 对money进行处理(比如保留2位小数位)
	 * 
	 * @param money
	 * @return
	 */
	public String money(Double money,WebContext webCtx);

}

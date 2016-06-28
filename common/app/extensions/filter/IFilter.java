package extensions.filter;

import play.libs.F.Promise;
import play.mvc.Result;
import play.mvc.Http.Context;

/**
 * 实现这个对象用来拦截所有 HTTP 请求。需要在Module实现IFilterExtension， 并在其接口方法中注册到 Multibinder。
 * 
 * @author kmtong
 *
 */
public interface IFilter {

	int priority();
	
	/**
	 * 拦截所有 HTTP 请求
	 * 
	 * @param context
	 * @param chain
	 *            call chain.executeNext() to continue the process
	 * @return null if filter chain should go on.
	 * @throws Throwable
	 */
	Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable;
}

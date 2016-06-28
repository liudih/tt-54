package extensions.tracking;

import context.WebContext;
import extensions.filter.FilterExecutionChain;
import play.libs.F.Promise;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.Context;


public interface IAffiliateIDTracking {

	public static final String ORIGIN_STRING = "AID";
	public static final String SOURCE_COOKIE = "SRC";

	public abstract int priority();

	public abstract Promise<Result> call(Context ctx) throws Throwable;

	public abstract Promise<Result> call(Http.Context ctx,
			FilterExecutionChain chain) throws Throwable;

	public abstract String getSource(Context ctx);
	
	public abstract String getSourceHost(Context ctx);
	
	public abstract String getAffiliateID(Context ctx);
	
	/**
	 * 给mobile调用
	 * @param ctx
	 * @return
	 * @author xiaoch
	 */
	public abstract String getAffiliateIDByWc(WebContext ctx);
	
	/**
	 * 给mobile调用
	 * @param ctx
	 * @return
	 * @author xiaoch
	 */
	public abstract String getSourceHostByWc(WebContext ctx);

}
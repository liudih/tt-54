package filter.base;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.HtmlUtils;
import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;

public class PageRenderTimingFilter implements IFilter {

	
	@Override
	public int priority() {
		return 1;
	}

	@Override
	public Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable {
		
		final long begin = System.currentTimeMillis();
		HtmlUtils.misc(context).addTail(
				() -> Html.apply("<!-- PageLoad: "
						+ (System.currentTimeMillis() - begin) + "ms -->"));
		return chain.executeNext(context);
	}

}

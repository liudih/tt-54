package extensions.vizury;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.SystemParameterService;
import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;

@Singleton
public class VizuryScriptAddition implements IFilter {

	@Inject
	SystemParameterService sysparam;

	@Override
	public int priority() {
		return 99;
	}

	@Override
	public Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable {
		String adname = "CN_Tomtop";
		String adid = "VIZVRM3152";
		views.html.vizury.vizury.render(adname, adid, context);
		return chain.executeNext(context);
	}

}

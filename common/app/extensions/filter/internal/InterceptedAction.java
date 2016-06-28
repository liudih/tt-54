package extensions.filter.internal;

import java.util.List;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

import com.google.common.collect.Lists;
import com.google.inject.Injector;

import extensions.filter.FilterExecutionChain;
import extensions.filter.FilterList;
import extensions.filter.IFilter;

public class InterceptedAction extends Action<Object> {

	final Injector injector;
	final List<IFilter> filters;

	public InterceptedAction(Action<?> original, Injector injector) {
		super();
		FilterList list = injector.getInstance(FilterList.class);
		this.delegate = original;
		this.injector = injector;
		this.filters = list.getFilters();
	}

	@Override
	public Promise<Result> call(Context context) throws Throwable {
		// R/W intercept here
		try {
			FilterExecutionChain chain = new FilterExecutionChain(
					Lists.newArrayList(filters), this.delegate);
			return chain.executeNext(context);
		} catch (Throwable e) {
			Logger.error("Intercepted Exception in FilterChain", e);
			throw e;
		}
	}

}

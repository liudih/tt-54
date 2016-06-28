package extensions.filter;

import java.util.List;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

import com.google.common.collect.Lists;

public class FilterExecutionChain {

	final List<IFilter> filters;
	final Action<?> delegate;

	public FilterExecutionChain(List<IFilter> filters, Action<?> delegate) {
		this.filters = filters != null ? Lists.newLinkedList(filters) : Lists
				.newLinkedList();
		this.delegate = delegate;
	}

	public Promise<Result> executeNext(Context context) throws Throwable {
		if (filters.size() > 0) {
			IFilter filter = filters.remove(0);
			return filter.call(context, this);
		} else {
			return delegate.call(context);
		}
	}
}

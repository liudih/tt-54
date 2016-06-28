package extensions.filter;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Ordering;

@Singleton
public class FilterList {

	final List<IFilter> filters;

	@Inject
	public FilterList(Set<IFilter> filters) {
		this.filters = Ordering.natural()
				.onResultOf((IFilter f) -> f.priority()).sortedCopy(filters);
	}

	public List<IFilter> getFilters() {
		return filters;
	}
}

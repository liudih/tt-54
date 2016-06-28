package com.tomtop.es.filters;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.FluentIterable;

/**
 * 
 * @author lijun
 *
 */
@Service
public class IndexFilterHelper {
	Set<IIndexFilter> sorted;

	@Autowired
	public IndexFilterHelper(Set<IIndexFilter> filters) {
		this.sorted = FluentIterable.from(filters).toSortedSet(
				new Comparator<IIndexFilter>() {

					@Override
					public int compare(IIndexFilter f1, IIndexFilter f2) {
						return f1.getPriority() - f2.getPriority();
					}

				});
	}

	public void handle(int lang, Map<String, Object> attributes) {
		FluentIterable.from(sorted).forEach(f -> {
			f.handle(lang, attributes);
		});
	}
}

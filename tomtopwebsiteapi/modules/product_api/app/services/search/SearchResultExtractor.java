package services.search;

import org.elasticsearch.search.SearchHit;

public interface SearchResultExtractor<T> {

	T extract(SearchHit hit);
}

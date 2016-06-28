package services.search;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;

import play.libs.F;
import play.libs.F.Promise;

/**
 * Bridge for Async Search
 * 
 * @author kmtong
 *
 */
public class SearchActionListener implements ActionListener<SearchResponse> {

	F.RedeemablePromise<SearchResponse> promise;

	public SearchActionListener() {
		promise = F.RedeemablePromise.empty();
	}

	@Override
	public void onFailure(Throwable ex) {
		promise.failure(ex);
	}

	@Override
	public void onResponse(SearchResponse result) {
		promise.success(result);
	}

	public Promise<SearchResponse> execute(SearchRequestBuilder request) {
		request.execute(this);
		return promise;
	}
}

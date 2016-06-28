package common;

import services.search.ISearchService;
import services.search.criteria.KeywordSearchCriteria;

import com.caucho.hessian.client.HessianProxyFactory;

import valueobjects.search.SearchContext;

public class HessianClientTest {

	public static void main(String[] args) throws Exception {
		HessianProxyFactory factory = new HessianProxyFactory();

		ISearchService s = (ISearchService) factory
				.create("http://localhost:9000/common/hs/search");
		SearchContext sc = new SearchContext();
		sc.getCriteria().add(new KeywordSearchCriteria("usb"));
		System.out.println("Out: " + s.search(sc, 1, 1));
	}
}

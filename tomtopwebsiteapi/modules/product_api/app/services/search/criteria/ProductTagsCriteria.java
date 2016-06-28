package services.search.criteria;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchCriteria;

public class ProductTagsCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 915373323548247114L;

	String productTag;

	public ProductTagsCriteria() {
	}

	public ProductTagsCriteria(String productTag) {
		this.productTag = productTag;
	}

	@Override
	public QueryBuilder getQueryBuilder() {

		BoolQueryBuilder bool = QueryBuilders.boolQuery();
		if (productTag == ProductLabelType.Clearstocks.toString()) {
			bool.must(QueryBuilders.termQuery("tags", productTag.toLowerCase()));
		} else {
			bool.should(QueryBuilders.termQuery("tags", productTag.toLowerCase())).mustNot(
					QueryBuilders.termQuery("tags",
							ProductLabelType.Clearstocks.toString()));
		}
		return bool;
	}
}

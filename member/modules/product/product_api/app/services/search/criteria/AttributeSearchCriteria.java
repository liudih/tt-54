package services.search.criteria;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchCriteria;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

public class AttributeSearchCriteria implements ISearchCriteria {

	private static final long serialVersionUID = -7052666859822804795L;

	List<String> attributes;
	/*
	 * color -> color:red, color:blue
	 */
	ListMultimap<String, String> attributeByKey;

	public AttributeSearchCriteria() {
		this.attributes = Lists.newArrayList();
	}

	public AttributeSearchCriteria(List<String> attributeValuePair) {
		this.attributes = Lists.newArrayList(attributeValuePair);
		this.attributeByKey = Multimaps.index(this.attributes,
				(String a) -> a.split(":")[0]);
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		BoolQueryBuilder bool = QueryBuilders.boolQuery();
		for (String key : attributeByKey.keySet()) {
			BoolQueryBuilder b = QueryBuilders.boolQuery();
			for (String attr : attributeByKey.get(key)) {
				b.should(QueryBuilders.termQuery("attributes", attr.toLowerCase()));
			}
			bool.must(b);
		}
		return bool;
	}

}

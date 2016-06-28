package services.search.criteria;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchKeyWords;

import com.google.common.collect.Lists;

public class KeywordSearchCriteria extends ISearchKeyWords {

	private static final long serialVersionUID = -8646955761914666186L;

	String keyword;

	public KeywordSearchCriteria() {
	}
	
	@Override
	public String getKeyWord(String keyword) {
		this.keyword = keyword;
		return keyword;
	}

	public KeywordSearchCriteria(String keyword) {
		this.keyword = this.getKeyWord(keyword);
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return QueryBuilders.queryString(preprocess(keyword));
	}

	public String getKeyword() {
		return keyword;
	}

	public static String preprocess(String keyword) {
		String result = keyword;
		if (keyword != null) {
			result = keyword.replaceAll("(\")|(/)|(\\\\)", " ");
			List<String> keywords = Lists.transform(
					Lists.newArrayList(result.split(" ")), w -> "+" + "\""
							+ w.trim().toLowerCase() + "\"");
			result = StringUtils.join(keywords, ' ');
		}
		return result;
	}

}

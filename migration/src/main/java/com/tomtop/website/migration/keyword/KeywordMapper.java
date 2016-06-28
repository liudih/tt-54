package com.tomtop.website.migration.keyword;

import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface KeywordMapper {
	
	@Select("SELECT * FROM catalogsearch_query ORDER BY query_id LIMIT #{0} OFFSET #{1}")
	public List<KeywordEntity> getKeywords(int limit, int offset);
	
}

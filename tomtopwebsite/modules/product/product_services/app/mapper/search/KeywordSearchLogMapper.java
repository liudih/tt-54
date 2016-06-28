package mapper.search;

import org.apache.ibatis.annotations.Insert;

import dto.search.KeywordSearchLog;

public interface KeywordSearchLogMapper {

	@Insert("INSERT INTO t_keyword_search_log "
			+ "(iwebsiteid, ilanguageid, ckeyword, iresults, cltc, cstc, cip, dcreatedate) "
			+ "VALUES (#{iwebsiteid},#{ilanguageid},#{ckeyword},#{iresults},#{cltc},#{cstc},#{cip},#{dcreatedate})")
	int insert(KeywordSearchLog log);
}

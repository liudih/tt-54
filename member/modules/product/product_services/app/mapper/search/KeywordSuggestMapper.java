package mapper.search;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.search.KeywordSearchLog;
import dto.search.KeywordSuggest;

public interface KeywordSuggestMapper {

	int insert(KeywordSuggest record);

	int updateByPrimaryKey(KeywordSuggest record);

	@Select("SELECT * FROM t_keyword_suggest "
			+ "WHERE ckeyword like #{0} AND iwebsiteid = #{1} AND ilanguageid = #{2} "
			+ "ORDER BY irank DESC LIMIT #{3}")
	List<KeywordSuggest> findByKeywordLike(String partial, int siteId,
			int languageId, int limit);

	@Select("SELECT * FROM t_keyword_suggest "
			+ "WHERE iwebsiteid = #{0} AND ilanguageid = #{1} "
			+ "ORDER BY irank DESC")
	List<KeywordSuggest> showHotProducts(int siteId, int languageId);

	@Select("SELECT  iid, ckeyword, irank, icategoryid, ilanguageid, iwebsiteid,cinfo,iresults,bmachine,"
			+ "ccreateuser, dcreatedate FROM t_keyword_suggest "
			+ "WHERE ckeyword=#{0} AND iwebsiteid = #{1} AND ilanguageid = #{2}")
	KeywordSuggest getKeywordSuggetByKeyWord(String keyWord, int websiteid,
			int languageid);

	@Insert("INSERT INTO t_keyword_search_log (ckeyword, iresults, cip, cltc, cstc, ilanguageid, iwebsiteid, dcreatedate)"
			+ "VALUES (#{ckeyword}, #{iresults}, #{cip}, #{cltc}, #{cstc}, #{ilanguageid}, #{iwebsiteid}, "
			+ "#{dcreatedate})")
	Integer insetKeywordSearchLog(KeywordSearchLog keywordSearchLog);

}

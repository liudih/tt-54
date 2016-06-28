package mapper.activitydb.page;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.activity.page.PageJoin;
import values.activity.page.PageJoinQuery;

/**
 * 活动参与映射接口
 * 
 * @author Guozy
 *
 */
public interface PageJoinMapper {

	@Select("select count(*) from t_page_join t where t.cjoiner=#{0} and t.ipageid=#{1}")
	Integer getPageJoinsBycjoinerAndIpageId(String cjoiner, Integer ipageid);

	@Insert("insert into t_page_join (ipageid,cjoiner,cjoinparam,iwebsiteid,cvhost,ccountry,cresult,dcreatedate) "
			+ "values (#{ipageid},#{cjoiner},#{cjoinparam},#{iwebsiteid},#{cvhost},#{ccountry},#{cresult},now())")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int addPageJoin(PageJoin pageJoin);

	@Select("<script>"+
			"select * from t_page_join "
			+ "where ipageid = #{activityPageId} "
			+ "<if test=\"memberID != null  \">and cjoiner = #{memberID} </if>"
			+ "and iwebsiteid = #{siteID} "
			+ "</script>")
	List<PageJoin> getJoinedCount(@Param("activityPageId") int activityPageId, @Param("memberID") String memberID,
			@Param("siteID") String string);

	@Select("select count(*) from (select distinct(cjoiner) from t_page_join where ipageid = #{activityPageId} and iwebsiteid = #{siteID}) c")
	Integer getJoinMemberCount(@Param("activityPageId") int activityPageId, @Param("siteID") String siteID);

}

package mapper.activitydb.page;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import values.activity.page.PageRuleQuery;
import entity.activity.page.PageRule;

public interface PageRuleMapper {
	@Delete({ "delete from t_page_rule", "where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Insert({
			"insert into t_page_rule (ipageid, ",
			"crule, cruleparam, ",
			"ccreateuser, dcreatedate, ",
			"ienable)",
			"values (#{ipageid,jdbcType=INTEGER}, ",
			"#{crule,jdbcType=VARCHAR}, #{cruleparam,jdbcType=VARCHAR}, ",
			"#{ccreateuser,jdbcType=VARCHAR}, #{dcreatedate,jdbcType=TIMESTAMP}, ",
			"#{ienable,jdbcType=INTEGER})" })
	int insert(PageRule record);

	@Select({
			"select",
			"iid, ipageid, crule, cruleparam, ccreateuser, dcreatedate, ienable",
			"from t_page_rule", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ipageid", property = "ipageid", jdbcType = JdbcType.INTEGER),
			@Result(column = "crule", property = "crule", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cruleparam", property = "cruleparam", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ccreateuser", property = "ccreateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dcreatedate", property = "dcreatedate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "ienable", property = "ienable", jdbcType = JdbcType.INTEGER) })
	PageRule selectByPrimaryKey(Integer iid);

	@Select({
			"select",
			"iid, ipageid, crule, cruleparam, ccreateuser, dcreatedate, ienable",
			"from t_page_rule" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ipageid", property = "ipageid", jdbcType = JdbcType.INTEGER),
			@Result(column = "crule", property = "crule", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cruleparam", property = "cruleparam", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ccreateuser", property = "ccreateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dcreatedate", property = "dcreatedate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "ienable", property = "ienable", jdbcType = JdbcType.INTEGER) })
	List<PageRule> selectAll();

	@Update({ "update t_page_rule",
			"set ipageid = #{ipageid,jdbcType=INTEGER},",
			"crule = #{crule,jdbcType=VARCHAR},",
			"cruleparam = #{cruleparam,jdbcType=VARCHAR},",
			"ienable = #{ienable,jdbcType=INTEGER}",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(PageRule record);

	@Select("select count(*) from t_page_rule where ipageid=#{pageid}")
	int getCountByPageid(Integer pageid);

	/**
	 * 获取分页总数
	 * 
	 * @param url
	 * @return
	 */
	@Select({ "<script>", "select count(*) from ",
			"t_page_rule t1 inner join t_page t2 on t1.ipageid=t2.iid",
			"where 1=1",
			"<if test=\"url != null and url != ''\"> and t2.curl=#{url}</if>",
			"</script>" })
	int getCount(@Param("url") String url);

	/**
	 * 获取分页
	 * 
	 * @param page
	 * @param pagesize
	 * @param url
	 * @return
	 */
	@Select({
			"<script>",
			"select t2.curl,t2.ienable as ipageienable,t1.* from ",
			"t_page_rule t1 inner join t_page t2 on t1.ipageid=t2.iid",
			"where 1=1",
			"<if test=\"url != null and url != ''\"> and t2.curl=#{url}</if>",
			" ORDER BY t2.curl limit #{pagesize} offset (#{page}-1)*#{pagesize} ",
			"</script>" })
	List<PageRuleQuery> getPage(@Param("page") int page,
			@Param("pagesize") int pagesize, @Param("url") String url);

	@Select("select * from t_page_rule where ipageid=#{pageid}")
	List<PageRule> getListByPageid(int pageid);
	
	/**
	 * 
	 * @param i 
	 * @Title: getPageRuleByPageId
	 * @Description: TODO(通过活动id查询活动规则)
	 * @param @param pageId
	 * @param @return
	 * @return List<PageRule>
	 * @throws 
	 * @author yinfei
	 */
	@Select("select * from t_page_rule where ipageid = #{pageId} and ienable = #{enable}")
	List<PageRule> getPageRuleByPageId(@Param("pageId")int pageId, @Param("enable")int i);
}
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

import values.activity.page.PagePrizeQuery;
import entity.activity.page.PagePrize;

public interface PagePrizeMapper {
	@Delete({ "delete from t_page_prize",
			"where iruleid = #{ruleid,jdbcType=INTEGER}" })
	int deleteByRuleid(Integer ruleid);

	@Delete({ "delete from t_page_prize", "where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Insert({
			"insert into t_page_prize (ipageid, ",
			"cname, ctype, ctypeparam, ",
			"ccreateuser, dcreatedate,isort, ",
			"ienable,cextraparam,iruleid)",
			"values (#{ipageid,jdbcType=INTEGER}, ",
			"#{cname,jdbcType=VARCHAR}, #{ctype,jdbcType=VARCHAR}, #{ctypeparam,jdbcType=VARCHAR}, ",
			"#{ccreateuser,jdbcType=VARCHAR}, #{dcreatedate,jdbcType=TIMESTAMP},#{isort}, ",
			"#{ienable,jdbcType=INTEGER},#{cextraparam},#{iruleid})" })
	int insert(PagePrize record);

	@Select({
			"select",
			"iid, ipageid, cname,isort,iruleid, ctype, ctypeparam, ccreateuser, dcreatedate, ienable,cextraparam",
			"from t_page_prize", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ipageid", property = "ipageid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cname", property = "cname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ctype", property = "ctype", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ctypeparam", property = "ctypeparam", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ccreateuser", property = "ccreateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dcreatedate", property = "dcreatedate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "ienable", property = "ienable", jdbcType = JdbcType.INTEGER) })
	PagePrize selectByPrimaryKey(Integer iid);

	@Select({
			"select",
			"iid, ipageid, cname,isort,iruleid, ctype, ctypeparam, ccreateuser, dcreatedate, ienable,cextraparam",
			"from t_page_prize" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ipageid", property = "ipageid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cname", property = "cname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ctype", property = "ctype", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ctypeparam", property = "ctypeparam", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ccreateuser", property = "ccreateuser", jdbcType = JdbcType.VARCHAR),
			@Result(column = "dcreatedate", property = "dcreatedate", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "ienable", property = "ienable", jdbcType = JdbcType.INTEGER) })
	List<PagePrize> selectAll();

	@Update({ "update t_page_prize",
			"set ipageid = #{ipageid,jdbcType=INTEGER},", "isort=#{isort},",
			"cname = #{cname,jdbcType=VARCHAR},", "iruleid = #{iruleid},",
			"ctype = #{ctype,jdbcType=VARCHAR},",
			"ctypeparam = #{ctypeparam,jdbcType=VARCHAR},",
			"cextraparam = #{cextraparam,jdbcType=VARCHAR},",
			"ienable = #{ienable,jdbcType=INTEGER}",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(PagePrize record);

	@Select("select *　from t_page_prize where ipageid=#{0}")
	List<PagePrize> getListByPageid(int pageid);

	/**
	 * 获取分页总数
	 * 
	 * @param url
	 * @return
	 */
	@Select({ "<script>", "select count(*) from ",
			"t_page_prize t1 inner join t_page t2 on t1.ipageid=t2.iid",
			"inner join t_page_rule t3 on t3.ipageid=t2.iid", "where 1=1",
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
			"select t2.curl,t2.ienable as ipageienable,t3.crule,t1.* from ",
			"t_page_prize t1 inner join t_page t2 on t1.ipageid=t2.iid",
			"inner join t_page_rule t3 on t3.ipageid=t2.iid",
			"where 1=1",
			"<if test=\"url != null and url != ''\"> and t2.curl=#{url}</if>",
			" ORDER BY t2.curl limit #{pagesize} offset (#{page}-1)*#{pagesize} ",
			"</script>" })
	List<PagePrizeQuery> getPage(@Param("page") int page,
			@Param("pagesize") int pagesize, @Param("url") String url);

	/**
	 * 
	 * @param i 
	 * @Title: getPPByPageId
	 * @Description: TODO(通过活动id查询活动结果)
	 * @param @param pageId
	 * @param @return
	 * @return List<PagePrize>
	 * @throws 
	 * @author yinfei
	 */
	@Select("select * from t_page_prize where ipageid=#{pageId} and ienable = #{enable}")
	List<PagePrize> getPPByPageId(@Param("pageId")int pageId, @Param("enable")int i);
}
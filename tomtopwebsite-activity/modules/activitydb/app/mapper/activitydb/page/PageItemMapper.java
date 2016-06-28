package mapper.activitydb.page;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import values.activity.page.PageItemQuery;
import entity.activity.page.PageItem;

public interface PageItemMapper {
	@Delete({ "delete from t_page_item", "where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Options(keyProperty = "iid", useGeneratedKeys = true)
	@Insert({
			"insert into t_page_item (ipageid, ",
			"cvalue, cimgurl,ipriority,",
			"cimgtargeturl)",
			"values (#{ipageid,jdbcType=INTEGER}, ",
			"#{cvalue,jdbcType=VARCHAR}, #{cimgurl,jdbcType=VARCHAR},#{ipriority},",
			"#{cimgtargeturl,jdbcType=VARCHAR})" })
	int insert(PageItem record);

	@Select({ "select",
			"iid, ipageid, cvalue, cimgurl, cimgtargeturl,ipriority",
			"from t_page_item", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ipageid", property = "ipageid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cvalue", property = "cvalue", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cimgurl", property = "cimgurl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cimgtargeturl", property = "cimgtargeturl", jdbcType = JdbcType.VARCHAR) })
	PageItem selectByPrimaryKey(Integer iid);

	@Select({ "select",
			"iid, ipageid, cvalue, cimgurl, cimgtargeturl,ipriority",
			"from t_page_item" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ipageid", property = "ipageid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cvalue", property = "cvalue", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cimgurl", property = "cimgurl", jdbcType = JdbcType.VARCHAR),
			@Result(column = "cimgtargeturl", property = "cimgtargeturl", jdbcType = JdbcType.VARCHAR) })
	List<PageItem> selectAll();

	@Update({ "update t_page_item",
			"set ipageid = #{ipageid,jdbcType=INTEGER},",
			"cvalue = #{cvalue,jdbcType=VARCHAR},",
			"cimgurl = #{cimgurl,jdbcType=VARCHAR},",
			"ipriority = #{ipriority},",
			"cimgtargeturl = #{cimgtargeturl,jdbcType=VARCHAR}",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(PageItem record);

	/**
	 * 获取总数，配合getPage用的
	 * 
	 * @param ienable
	 * @param url
	 * @param type
	 * @return
	 */
	@Select({
			"<script> ",
			" select count(*) from t_page_item t1 inner join t_page t2 on t1.ipageid=t2.iid where 1=1",
			" <if test=\"url != null and url != '' \"> and	t2.curl = #{url} </if>",
			" <if test=\"type != null\">and t2.itype = #{type}</if>",
			" <if test=\"ienable != null\"> and t2.ienable = #{ienable}	</if>",
			"</script>" })
	int getCount(@Param("ienable") Integer ienable, @Param("url") String url,
			@Param("type") Integer type);

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页面大小
	 * @param ienable
	 *            查询条件是否启用
	 * @param url
	 *            查询条件主题url
	 * @param iid
	 *            页面类型
	 * @return
	 */
	@Select({
			"<script> ",
			" select t1.iid,t1.ipriority,t1.ipageid, t1.cvalue, t1.cimgurl, t1.cimgtargeturl,t2.curl,t2.ienable,t2.itype ",
			" from t_page_item t1 inner join t_page t2 on t1.ipageid=t2.iid where 1=1",
			" <if test=\"url != null and url != '' \"> and	t2.curl = #{url} </if>",
			" <if test=\"type != null\">and t2.itype = #{type}</if>",
			" <if test=\"ienable != null\"> and t2.ienable = #{ienable}	</if>",
			" ORDER BY iid limit #{pagesize} offset (#{page}-1)*#{pagesize} ",
			"</script>" })
	List<PageItemQuery> getPage(@Param("page") int page,
			@Param("pagesize") int pagesize, @Param("ienable") Integer ienable,
			@Param("url") String url, @Param("type") Integer type);

	/**
	 * 
	 * @Title: getPageItemByPageId
	 * @Description: TODO(通过活动id获取活动项目)
	 * @param @param iid
	 * @param @return
	 * @return List<PageItem>
	 * @throws 
	 * @author yinfei
	 */
	@Select("select * from t_page_item where ipageid = #{pageId}")
	List<PageItem> getPageItemByPageId(@Param("pageId") Integer iid);
}
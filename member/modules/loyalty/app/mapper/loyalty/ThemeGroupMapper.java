package mapper.loyalty;

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

import entity.loyalty.ThemeGroup;
import forms.loyalty.theme.template.ThemeGroupForm;

public interface ThemeGroupMapper {
	@Delete({ "delete from t_theme_group",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Options(useGeneratedKeys = true, keyProperty = "iid")
	@Insert({ "insert into t_theme_group (ithemeid, ", "isort,curl) ",
			"values (#{ithemeid,jdbcType=INTEGER}, ",
			"#{isort,jdbcType=INTEGER},#{curl,jdbcType=VARCHAR})" })
	int insert(ThemeGroupForm record);

	@Select("<script>" + "select count(iid) from t_theme_group where 1=1 "
			+ "<if test=\"iid != null  \">and iid=#{iid} </if>"
			+ "<if test=\"ithemeid != null  \">and ithemeid=#{ithemeid} </if>"
			+ "</script>")
	Integer getCount(@Param("iid") Integer iid,
			@Param("ithemeid") Integer ithemeid);

	@Select("<script>"
			+ "select * from t_theme_group where 1=1 "
			+ "<if test=\"iid != null  \">and iid=#{iid} </if>"
			+ "<if test=\"ithemeid != null  \">and ithemeid=#{ithemeid} </if> ORDER BY Iid desc limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<ThemeGroup> getThemeGroups(@Param("iid") Integer iid,
			@Param("ithemeid") Integer ithemeid,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select({ "select", "iid, ithemeid, isort", "from t_theme_group",
			"where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ithemeid", property = "ithemeid", jdbcType = JdbcType.INTEGER),
			@Result(column = "isort", property = "isort", jdbcType = JdbcType.INTEGER) })
	ThemeGroup selectByPrimaryKey(Integer iid);

	@Select({ "select", "iid, ithemeid, isort", "from t_theme_group" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "ithemeid", property = "ithemeid", jdbcType = JdbcType.INTEGER),
			@Result(column = "isort", property = "isort", jdbcType = JdbcType.INTEGER) })
	List<ThemeGroup> selectAll();

	@Options(useGeneratedKeys = true, keyProperty = "iid")
	@Update({ "update t_theme_group",
			"set ithemeid = #{ithemeid,jdbcType=INTEGER},",
			"isort = #{isort,jdbcType=INTEGER},",
			"curl = #{curl,jdbcType=VARCHAR}",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int updateByPrimaryKey(ThemeGroup record);

	@Select("select iid,ithemeid,isort,curl from t_theme_group where iid=#{0}")
	ThemeGroup getThemeGroupByIid(int iid);

	/**
	 * 根具主题id获取分组
	 * 
	 * @param themeid
	 * @return
	 */
	@Select({
			"select t1.iid,t3.cname,t1.ithemeid",
			"from t_theme_group t1 inner join t_theme t2 on t1.ithemeid=t2.iid",
			"inner join t_theme_group_name t3 on t3.igroupid=t1.iid and ilanguageid=1",
			"where t2.curl=#{themeurl}"
	})
	public List<ThemeGroupForm> getGroupByThemeurl(@Param("themeurl")String themeurl);

	@Select("select * from t_theme_group where ithemeid = #{themeId} order by isort")
	List<ThemeGroup> selectTGByThemeId(@Param("themeId")Integer iid);
	
	@Select({
		"select t1.iid,t3.cname,t1.ithemeid",
		"from t_theme_group t1 inner join t_theme t2 on t1.ithemeid=t2.iid",
		"inner join t_theme_group_name t3 on t3.igroupid=t1.iid and ilanguageid=1",
		"where t2.iid=#{themeid}"
	})
	public List<ThemeGroupForm> getGroupByThemeid(
			@Param("themeid") Integer themeid);

}
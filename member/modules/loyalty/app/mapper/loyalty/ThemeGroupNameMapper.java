package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import entity.loyalty.ThemeGroupName;

public interface ThemeGroupNameMapper {
	@Delete({ "delete from t_theme_group_name",
			"where iid = #{iid,jdbcType=INTEGER}" })
	int deleteByPrimaryKey(Integer iid);

	@Insert("insert into t_theme_group_name (igroupid,cname,ilanguageid) values (#{igroupid},#{cname},#{ilanguageid})")
	int insert(ThemeGroupName record);

	@Select({ "select", "iid, igroupid, cname, ilanguageid",
			"from t_theme_group_name", "where iid = #{iid,jdbcType=INTEGER}" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "igroupid", property = "igroupid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cname", property = "cname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ilanguageid", property = "ilanguageid", jdbcType = JdbcType.INTEGER) })
	ThemeGroupName selectByPrimaryKey(Integer iid);

	@Select({ "select", "iid, igroupid, cname, ilanguageid",
			"from t_theme_group_name" })
	@Results({
			@Result(column = "iid", property = "iid", jdbcType = JdbcType.INTEGER, id = true),
			@Result(column = "igroupid", property = "igroupid", jdbcType = JdbcType.INTEGER),
			@Result(column = "cname", property = "cname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "ilanguageid", property = "ilanguageid", jdbcType = JdbcType.INTEGER) })
	List<ThemeGroupName> selectAll();

	@Update("update t_theme_group_name set igroupid = #{igroupid},cname = #{cname},ilanguageid = #{ilanguageid} where iid = #{iid}")
	int updateByPrimaryKey(ThemeGroupName record);
	
	@Select("select * from t_theme_group_name where igroupid=#{0}")
	List<ThemeGroupName> getThemeGroupNamesByThemeGroupId(int themeGroupId);

	@Select("select * from t_theme_group_name where igroupid = #{groupId}")
	ThemeGroupName selectByGroupId(@Param("groupId") Integer groupId);

	/**
	 * 
	 * @Title: selectByGroupIdAndLanguageId
	 * @Description: TODO(通过专题组id和语言id查询专题组名称)
	 * @param @param iid
	 * @param @param languageId
	 * @param @return
	 * @return ThemeGroupName
	 * @throws 
	 * @author yinfei
	 */
	@Select("select * from t_theme_group_name where igroupid = #{groupId} and ilanguageid = #{languageId}")
	ThemeGroupName selectByGroupIdAndLanguageId(@Param("groupId")Integer iid, @Param("languageId")int languageId);

}